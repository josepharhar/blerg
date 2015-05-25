package main;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import common.ControlState;
import server.ConnectionManager;
import server.Game;

public class BlergServer {
    public static void main(String args[]) {
        System.out.println("Initializing server");
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(4);
        ObjectMapper mapper = new ObjectMapper();
        
        Game game = new Game(executor);
        ConnectionManager connectionManager = new ConnectionManager(mapper, game, executor);
        
        game.startGame();
        connectionManager.beginAcceptingConnections();
        
    }
}
