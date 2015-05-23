package networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;

import common.ControlState;
import common.Entity;

public class ServerNetworking {
    
    private static class SocketHolder {
        public Socket socket;
        public long entityID;
    }

    private List<Entity> entityList;
    private List<SocketHolder> activeClients;
    private ServerSocket serverSocket;

    private HashMap<Long, ControlState> playerControls;
    
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
        Timer timer = new Timer();
        TimerTask doNetworking = new TimerTask() {
            @Override
            public void run() {
                doNetworkTick();
            }
        };
        timer.scheduleAtFixedRate(doNetworking, 200, 200);
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public HashMap<Long, ControlState> getLatestControls() {
        return playerControls;
    }

    private void doNetworkTick() {
        Socket newConnection = null;
        try {
            newConnection = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (newConnection != null) {
            SocketHolder sh = new SocketHolder();
            sh.socket = newConnection;
            sh.entityID = System.currentTimeMillis();
            activeClients.add(sh);
        }
        
        Iterator<SocketHolder> iter = activeClients.iterator();
        
        List<Entity> toSend = new ArrayList<>(entityList);
        byte[] data = null;
        try {
            data = mapper.writeValueAsBytes(toSend);
            String data2 = mapper.writeValueAsString(toSend);
            System.out.println("data2: " + data2);
        } catch (Exception ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        
        while (iter.hasNext()) {
            try {
            SocketHolder socketHolder = iter.next();
            Socket socket = socketHolder.socket;
            if (socket.isClosed()) {
                iter.remove();
                continue;
            }
                InputStream input = socket.getInputStream();
                while (input.available() > 0) {
                    ControlState state = mapper.readValue(input, ControlState.class);
                    playerControls.put(socketHolder.entityID, state);
                }
                OutputStream output = socket.getOutputStream();
                UpdateHeader header = new UpdateHeader();
                header.setYouAre(socketHolder.entityID);
                header.setObjectCount(toSend.size());
                mapper.writeValue(output, header);
                output.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
