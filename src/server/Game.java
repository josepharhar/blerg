package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import common.Entity;
import common.Entity.EntityType;

public class Game {
    
    private volatile long nextAvailableIdentifier = 0;
    
    private ScheduledExecutorService executor;
    private List<Entity> entities = new ArrayList<Entity>();
    
    public Game(ScheduledExecutorService executor) {
        this.executor = executor;
    }

    public void startGame() {
        Entity testFoodParticle = new Entity(1, 10, 10, 0, 0, 5, 255, 0, 0, EntityType.FOOD);
        entities.add(testFoodParticle);
        executor.scheduleAtFixedRate(() -> updateGameState(), 100, 100, TimeUnit.MILLISECONDS);
    }

    private void updateGameState() {
        SuperCollider.collide(entities);

        for (Entity entity : entities) {
            entity.update();
        }
    }
    
    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity newEntity) {
        entities.add(newEntity);
    }
    
    public long getUniqueIdentifier() {
        return nextAvailableIdentifier++;
    }

}
