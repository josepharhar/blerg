package game;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    
    private List<Entity> entities;
	
	public void startGame() {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				updateGameState();
			}
			
		};
		
		timer.scheduleAtFixedRate(task, 100, 100);
	}
	
	private void updateGameState() {
		for (Entity entity : entities) {
		    entity.update();
		}
	}
	
	private void checkCollision() {
	    for (Entity entity : entities) {
	        
	    }
	}
    
}
