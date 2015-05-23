package networking;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;

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
                System.out.println("doingTick");
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
                if (socket.getInputStream().available() > 0) {
                    InputStream input = socket.getInputStream();
                    UpdateHeader header = mapper.readValue(input, UpdateHeader.class);
                    playerID = header.getYouAre();
                    latestObjectList = (List<Entity>) mapper.readValue(input, List.class);
                }
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
                output.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }).start();
        if (socket.isClosed()) {
            throw new RuntimeException("Connection to server lost");
        }
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
