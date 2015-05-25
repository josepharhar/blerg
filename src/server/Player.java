package server;

import common.ControlState;
import common.Entity;

public class Player extends Entity {
    private volatile ControlState controlState = new ControlState();
    
    public Player(long id) {
        super(  id,
                Math.random() * 800,
                Math.random() * 800,
                0,
                0,
                10,
                (int) (Math.random() * 128 + 127),
                (int) (Math.random() * 128 + 127),
                (int) (Math.random() * 128 + 127),
                EntityType.PLAYER);
    }
    
    public void update() {
        double yScale = Math.sin(controlState.getDirection());
        double xScale = Math.cos(controlState.getDirection());
        double yChange = yScale * controlState.getMagnitude();
        double xChange = xScale * controlState.getMagnitude();
        yVelocity += yChange;
        xVelocity += xChange;
        yVelocity *= .6;
        xVelocity *= .6;
        super.update();
    }
    
    public void setControlState(ControlState controlState) {
        this.controlState = controlState;
    }
}
