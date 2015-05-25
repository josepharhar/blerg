package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import common.ControlState;
import common.Entity;
import common.networking.UpdateFooter;

public class ClientNetworking {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private Socket socket;
    private volatile List<Entity> latestObjectList = new ArrayList<>();
    private volatile ControlState controlState;
    private volatile long playerID;
    
    public ClientNetworking(ControlState controlState) {
        this.controlState = controlState;
        
        try {
            socket = new Socket("127.0.0.1", 5080);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        Timer timer = new Timer();
        TimerTask doNetworking = new TimerTask() {
            @Override
            public void run() {
                doNetworkTick();
            }
        };
        timer.schedule(doNetworking, 100, 100);
    }

    public List<Entity> getLatestState() {
        return latestObjectList;
    }
    
    public void doNetworkTick() {
        if (socket.isClosed()) {
            throw new RuntimeException("Connection to server lost");
        }
        new Thread(() -> {
            try {
                InputStream stream = socket.getInputStream();
                int contentAmt = stream.available();
                byte[] bytes = new byte[contentAmt];
                stream.read(bytes);
                String str = new String(bytes);
                String stuff[] = str.split("\0");
                List<Entity> newList = new ArrayList<>();
                HashMap<Long, Entity> newEntities = new HashMap<>();
                for (String s : stuff) {
                    if (s.contains("{")) {
                        try {
                            Entity entity = mapper.readValue(s, Entity.class);
                            newEntities.put(entity.getEntityID(), entity);
                        } catch (JsonMappingException ex) {
                            playerID = mapper.readValue(s, UpdateFooter.class).getYouAre();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        }
                    }
                }
                for (Map.Entry<Long, Entity> entry : newEntities.entrySet()) {
                    newList.add(entry.getValue());
                }
                latestObjectList = newList;
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }).start();
        new Thread(() -> {
            try {
                OutputStream output = socket.getOutputStream();
                byte[] toWrite = mapper.writeValueAsBytes(controlState);
                output.write(toWrite);
                output.write(0);
                output.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }).start();
    }
    
    public Entity getCurrentPlayer() {
        for (Entity entity : latestObjectList) {
            if (entity.getEntityID() == playerID) {
                return entity;
            }
        }
        
        return null;
    }
}
