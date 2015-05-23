package networking;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import server.Player;
import common.ControlState;
import common.Entity;

public class ClientNetworking {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private Socket socket;
    private volatile List<Entity> latestObjectList = new ArrayList<>();
    private volatile ControlState controlState;
    private volatile long playerID;
    
    public ClientNetworking(ControlState controlState) {
        this.controlState = controlState;
        
        try {
            socket = new Socket("127.0.0.1", 50805);
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
        timer.schedule(doNetworking, 800, 800);
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
                System.out.println("Attempting read");
                InputStream stream = socket.getInputStream();
                int contentAmt = stream.available();
                byte[] bytes = new byte[contentAmt];
                stream.read(bytes);
                String str = new String(bytes);
                System.out.println(str);
                String stuff[] = str.split("\0");
                List<Entity> newList = new ArrayList<>();
                for (String s : stuff) {
                    if (s.contains("{")) {
                        System.out.println("reading value: \'" + s + "\'");
                        try {
                            playerID = mapper.readValue(s, UpdateHeader.class).getYouAre();
                        } catch (JsonMappingException ex) {
                            newList.add(mapper.readValue(s, Entity.class));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        }
                    }
                }
                latestObjectList = newList;
                /*
                if (socket.getInputStream().available() > 0) {
                    System.out.println("performing read");
                    InputStream input = socket.getInputStream();
                    
                    JsonParser parser = jsonFactory.createJsonParser(input);
                    UpdateHeader header = parser.readValueAs(UpdateHeader.class);
                    System.out.println("read something");
                    playerID = header.getYouAre();
                    latestObjectList = (List<Entity>) parser.readValueAs(List.class);
                }
                System.out.println(new String());*/
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }).start();/*
        new Thread(() -> {
            try {
                OutputStream output = socket.getOutputStream();
                byte[] toWrite = mapper.writeValueAsBytes(controlState);
                output.write(toWrite);
                output.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }).start();*/
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
