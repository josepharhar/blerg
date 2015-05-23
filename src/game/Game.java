package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import collision.SuperCollider;

public class Game {
    
    private List<Entity> entities;
    
    public Game() {
        entities = new ArrayList<Entity>();
    }
	
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
	    SuperCollider.collide(entities);
	    
		for (Entity entity : entities) {
		    entity.update();
		}
	}
	
	public void addEntity(Entity newEntity) {
	    entities.add(newEntity);
	}
    
}
