package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.paint.Color;

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
            serverSocket.setSoTimeout(100);
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
        networkTimer.scheduleAtFixedRate(doNetworkingRead, 200, 1000);
        Player player = new Player();
        player.setRadius(20);
        player.setxLocation(50);
        player.setyLocation(50);
        player.setColor(Color.CHARTREUSE);
        entityList.add(player);
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public HashMap<Long, ControlState> getLatestControls() {
        return playerControls;
    }

    private void doNetworkTick() {
        String data;
        try {
            StringBuilder dataString = new StringBuilder();
            for (Entity entity : entityList) {
                dataString.append(mapper.writeValueAsString(entity));
                dataString.append('\0');
            }
            data = dataString.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        new Thread(() -> {
            try {
                Socket sock = serverSocket.accept();
                long id = System.currentTimeMillis();
                SocketHolder holder = new SocketHolder();
                holder.socket = sock;
                holder.entityID = id;
                activeClients.add(holder);
            } catch (SocketTimeoutException ex) {
                //expected
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }).start();
        for (SocketHolder sh : activeClients) {
            Socket socket = sh.socket;
            if (!socket.isClosed()) {/*
                new Thread(() -> {
                    try {
                        InputStream is = socket.getInputStream();
                        ControlState state = null;
                        while (!socket.isClosed() && is.available() > 0) {
                            state = mapper.readValue(is, ControlState.class);
                        }
                        if (state != null) {
                            playerControls.put(sh.entityID, state);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                }).start();*/
                new Thread(() -> {
                    try {
                        UpdateHeader header = new UpdateHeader();
                        header.setYouAre(sh.entityID);
                        OutputStream os = socket.getOutputStream();
                        os.write(mapper.writeValueAsBytes(header));
                        os.write(0);
                        os.write(data.getBytes());
                        os.flush();
                    } catch (Exception ex) {
                       // ex.printStackTrace();
                        //throw new RuntimeException(ex);
                    }
                }).start();
            }
        }
    }
}
