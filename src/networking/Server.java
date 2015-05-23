package networking;

import game.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import common.ControlState;

public class Server {

    private List<Entity> entityList;
    private List<Socket> activeClients;
    private ServerSocket serverSocket;

    private HashMap<Long, ControlState> playerControls;

    public Server() {
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
        timer.scheduleAtFixedRate(doNetworking, 100, 100);
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
            activeClients.add(newConnection);
        }
        
        Iterator<Socket> iter = activeClients.iterator();
        
        while (iter.hasNext()) {
            try {
            Socket socket = iter.next();
            if (socket.isClosed()) {
                iter.remove();
                continue;
            }
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
