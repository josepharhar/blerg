package server;

import common.Entity;

import javafx.scene.paint.Color;

public class Player implements Entity {

    private Position pos;
    private double r;
    
    private double vx;
    private double vy;
    private long entityID;
    
    public Player(double x, double y, long entityID) {
        pos = new Position(x, y);
        this.entityID = entityID;
    }
    
    public double getx() {
        return pos.x;
    }
    
    public double gety() {
        return pos.y;
    }
    
    public Position getLocation() {
        return pos;
    }
    
    public double getr() {
        return r;
    }
    
    public void setRadius(double radius) {
        r = radius;
    }
    
    public void update() {
        this.pos.x += this.vx;
        this.pos.y += this.vy;
    }
    
    public void collide(Entity other) {
        other.collide(this);
        
        // poor deflection physics
        this.vx *= -1;
        this.vy *= -1;
    }

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public long getEntityID() {
		return entityID;
	}

}
