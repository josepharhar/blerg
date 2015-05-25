package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectReader;

import common.ControlState;
import common.networking.UpdateFooter;

public class Client {
    private Socket socket;
    private Game game;
    private NetworkPacketCreator packetCreator;
    private ObjectReader controlStateReader;
    
    private volatile boolean isConnected = true;
    private long id;
    private Player player;
    
    public Client(Socket socket, Game game, ScheduledExecutorService executor, NetworkPacketCreator packetCreator, ObjectReader controlStateReader) throws Exception {
        this.socket = socket;
        this.game = game;
        this.packetCreator = packetCreator;
        this.controlStateReader = controlStateReader;
        this.id = game.getUniqueIdentifier();
        player = new Player(id);
        game.addEntity(player);
        
        executor.scheduleWithFixedDelay(() -> sendGameState(), 100, 100, TimeUnit.MILLISECONDS);
        executor.scheduleWithFixedDelay(() -> getControlState(), 100, 100, TimeUnit.MILLISECONDS);
    }
    
    private void getControlState() {
        if (socket.isClosed()) {
            isConnected = false;
        }
        if (isConnected) {
            try {
                InputStream is = socket.getInputStream();
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                String input = new String(bytes);
                for (String jsonControlState : input.split("\0")) {
                    if (jsonControlState.contains("{")) {
                        ControlState state = controlStateReader.readValue(jsonControlState);
                        player.setControlState(state);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    private void sendGameState() {
        if (socket.isClosed()) {
            isConnected = false;
        }
        if (isConnected) {
            try {
                UpdateFooter header = new UpdateFooter();
                header.setYouAre(id);
                String toSend = packetCreator.getPacket(header);
                OutputStream os = socket.getOutputStream();
                os.write(toSend.getBytes());
            } catch (Exception ex) {
                System.out.println("Caught exception while sending status to client: " + ex.getMessage());
                isConnected = false;
            }
        }
    }
}
