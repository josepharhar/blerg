package networking;

import game.Entity;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;

public class Networking {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private List<Entity> latestObjectList = new ArrayList<>();
    private Socket socket;
    
    public Networking() {
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
        timer.schedule(doNetworking, 100, 40);
    }

    public List<Entity> getLatestState() {
        return latestObjectList;
    }
    
    public void doNetworkTick() {
        if (socket.isClosed()) {
            throw new RuntimeException("Connection to server lost");
        }
        try {
            OutputStream output = socket.getOutputStream();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            List<Entity> newObjectList = new ArrayList<Entity>();
            if (socket.getInputStream().available() > 0) {
                InputStream input = socket.getInputStream();
                UpdateHeader header = mapper.readValue(input, UpdateHeader.class);
                for (int i = 0; i < header.getObjectCount(); i++) {
                    newObjectList.add(mapper.readValue(input, Entity.class));
                }
            }
            latestObjectList = newObjectList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
