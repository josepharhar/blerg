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
        
        grid.add(canvas, 0, 0);
        
        Timer timer = new Timer();
        
        TimerTask updateDrawing = new TimerTask() {
			@Override
			public void run() {
				updateCanvas(canvas.getGraphicsContext2D());
			}
        };
        
        timer.scheduleAtFixedRate(updateDrawing, 16, 16);
        
        return grid;
    }
    
    private void updateCanvas(GraphicsContext g) {
    	if (networking == null) {
    		return;
    	}
    	g.setFill(Color.GREY);
    	g.fillRect(0, 0, 800, 800);
    	for (Entity entity : networking.getLatestState()) {
    		g.setFill(entity.getColor());
    		g.fillOval(entity.getx() - entity.getr() / 2, entity.gety() - entity.getr() / 2, entity.getr(), entity.getr());
    	}
    }
    
    private void mouseMoved(MouseEvent event) {
        System.out.println("x: " + event.getX() + "\ny: " + event.getY());
        Entity player = networking.getCurrentPlayer();
        double dx = event.getX() - player.getx();
        double dy = event.getY() - player.gety();
        // TODO: account for different quadrants here
        double angle = Math.atan(dy / dx);
        controlState.setDirection(angle);
    }
    
}
