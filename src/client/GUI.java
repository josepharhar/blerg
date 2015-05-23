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
import networking.ClientNetworking;

import common.ControlState;
import common.Entity;

public class GUI extends Application {

    private Canvas canvas;
    private ClientNetworking networking;
    private volatile ControlState controlState;

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
<<<<<<< HEAD

=======
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                keyPressed(event);
            }
        });
        
>>>>>>> d83df3c984714e017217b094fd0dd55526e38754
        grid.add(canvas, 0, 0);

        Timer timer = new Timer();

        TimerTask updateDrawing = new TimerTask() {
            @Override
            public void run() {
                updateCanvas(canvas.getGraphicsContext2D());
            }
        };

        timer.scheduleAtFixedRate(updateDrawing, 33, 33);

        return grid;
    }

    private void updateCanvas(GraphicsContext g) {
        if (networking == null) {
            return;
        }
        g.setFill(Color.GREY);
        g.fillRect(0, 0, 800, 800);
        for (Entity entity : networking.getLatestState()) {
            System.out.println("Drawing entity");
            
            g.setFill(entity.getColor());
            g.fillRect(10, 10, 100, 100);
            g.setFill(Color.AQUA);
            g.fillOval(
                    entity.getxLocation() - entity.getRadius() / 2,
                    entity.getyLocation() - entity.getRadius() / 2,
                    entity.getRadius(), 
                    entity.getRadius());
        }
    }
<<<<<<< HEAD

    private void mouseMoved(MouseEvent event) {
        /*
         * // System.out.println("x: " + event.getX() + "\ny: " + event.getY());
         * Entity player = networking.getCurrentPlayer(); double dx =
         * event.getX() - player.getx(); double dy = event.getY() -
         * player.gety(); // TODO: account for different quadrants here double
         * angle = Math.atan(dy / dx); controlState.setDirection(angle);
         */
=======
    
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
//        System.out.println("x: " + event.getX() + "\ny: " + event.getY());
        Entity player = networking.getCurrentPlayer();
        double dx = event.getX() - player.getx();
        double dy = event.getY() - player.gety();
        double distance = Math.sqrt(dx * dx + dy * dy);
        controlState.setMagniture(distance > 10 ? 10 : distance);
        // TODO: account for different quadrants here
        double angle = Math.atan(dy / dx);
        controlState.setDirection(angle);
>>>>>>> d83df3c984714e017217b094fd0dd55526e38754
    }

}
