package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;

import common.ControlState;

public class ConnectionManager {
    
    private ObjectMapper mapper;
    private Game game;
    private ScheduledExecutorService executor;
    private ServerSocket serverSocket;
    private NetworkPacketCreator networkPacketCreator;

    public ConnectionManager(ObjectMapper mapper, Game game, ScheduledExecutorService executor) {
        this.mapper = mapper;
        this.game = game;
        this.executor = executor;
        this.networkPacketCreator = new NetworkPacketCreator(mapper, game, executor);
    }
    
    public void beginAcceptingConnections() {
        
        try {
            serverSocket = new ServerSocket(50805);
            executor.scheduleWithFixedDelay(() -> acceptPendingConnection(), 100, 100, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            System.out.println("Exception during network initialization: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        
    }
    
    private void acceptPendingConnection() {
        try {
            Socket sock = serverSocket.accept();
            new Client(sock, game, executor, networkPacketCreator, mapper.reader(ControlState.class));
        } catch (SocketTimeoutException ex) {
            System.out.println("Socket accept timed out");
        } catch (Exception ex) {
            System.out.println("Caught exception accepting new client: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
