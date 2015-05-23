package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage arg0) throws Exception {
        arg0.setTitle("The Blerg!");
        arg0.setScene(new Scene(buildWindow(), 300, 275));
        arg0.show();
        
    }
    
    public Parent buildWindow() {
        GridPane grid = new GridPane();
        
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25)); // edge padding
        grid.setHgap(10); // horizontal space between objects
        grid.setVgap(10); // vertical ^^
        
        grid.add(new Button("asdf"), 0, 0);
        
        return grid;
    }
    
}
