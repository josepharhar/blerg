package game;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
	
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
		
	}
    
}
