package client;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import common.ControlState;
import common.Entity;

public class GUI extends Application {

    private Canvas canvas;
    private ClientNetworking networking;
    private volatile ControlState controlState;
    
    private double targetX;
    private double targetY;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage arg0) throws Exception {
        arg0.setTitle("The Blerg!");
        arg0.setScene(new Scene(buildWindow(), 800, 800));
        arg0.show();
        controlState = new ControlState();
        networking = new ClientNetworking(controlState);
    }

    public Parent buildWindow() {
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25)); // edge padding
        grid.setHgap(10); // horizontal space between objects
        grid.setVgap(10); // vertical ^^

        canvas = new Canvas(800, 800);
        
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                mouseMoved(event);
            }
        });
        
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                keyPressed(event);
            }
        });
        
        grid.add(canvas, 0, 0);

        Timer timer = new Timer();

        TimerTask updateState = new TimerTask() {
            @Override
            public void run() {
                updateState(canvas.getGraphicsContext2D());
            }
        };

        timer.scheduleAtFixedRate(updateState, 20, 20);

        return grid;
    }

    private void updateState(GraphicsContext g) {
        if (networking == null) {
            return;
        }
        
        Entity player = networking.getCurrentPlayer();
        if (player != null) {
            double dx = targetX - player.getxLocation();
            double dy = targetY - player.getyLocation();
            double distance = Math.sqrt(dx * dx + dy * dy);
            controlState.setMagnitude(distance / 60 > 1 ? 1 : distance / 60);
            double angle = Math.atan2(dy, dx);
            
            controlState.setDirection(angle);
        }
        
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 800, 800);
        for (Entity entity : networking.getLatestState()) {
            g.setFill(entity.getColor());
            g.fillOval(
                    entity.getxLocation() - entity.getRadius() / 2,
                    entity.getyLocation() - entity.getRadius() / 2,
                    entity.getRadius(), 
                    entity.getRadius());
            
        }
    }
    private void keyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code.equals(KeyCode.SPACE)) {
            System.out.println("SPACE");
            controlState.setSplitting(true);
        } else if (code.equals(KeyCode.W)) {
            System.out.println("W");
            controlState.setShooting(true);
        }
    }
    
    private void mouseMoved(MouseEvent event) {
        targetX = event.getX();
        targetY = event.getY();
    }

}
