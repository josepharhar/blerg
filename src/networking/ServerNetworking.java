package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import server.Player;
import common.ControlState;
import common.Entity;

public class ServerNetworking {
    
    private static class SocketHolder {
        public Socket socket;
        public long entityID;
    }

    private List<Entity> entityList = new ArrayList<>();
    private List<SocketHolder> activeClients = new ArrayList<>();
    private ServerSocket serverSocket;

    private HashMap<Long, ControlState> playerControls = new HashMap<>();
    
    private ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String args[]) {
        ServerNetworking server = new ServerNetworking();
    }

    public ServerNetworking() {
        try {
            serverSocket = new ServerSocket(50805);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Timer networkTimer = new Timer();
        TimerTask doNetworkingRead = new TimerTask() {
            @Override
            public void run() {
                doNetworkTick();
            }
        };
        networkTimer.scheduleAtFixedRate(doNetworkingRead, 200, 200);
        Player player = new Player(40, 50, 100002);
        player.setRadius(5);
        entityList.add(player);
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public HashMap<Long, ControlState> getLatestControls() {
        return playerControls;
    }

    private void doNetworkTick() {
        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(entityList);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        for (SocketHolder sh : activeClients) {
            Socket socket = sh.socket;
            if (!socket.isClosed()) {
                new Thread(() -> {
                    try {
                        InputStream is = socket.getInputStream();
                        ControlState state = mapper.readValue(is, ControlState.class);
                        playerControls.put(sh.entityID, state);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                }).start();
                new Thread(() -> {
                    try {
                        UpdateHeader header = new UpdateHeader();
                        header.setYouAre(sh.entityID);
                        OutputStream os = socket.getOutputStream();
                        os.write(mapper.writeValueAsBytes(header));
                        os.write(bytes);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                }).start();
            }
        }
    }
}
