package game;

import collision.Position;

public class Player implements Entity {

    private Position pos;
    private double r;
    
    private double vx;
    private double vy;
    
    public Player(double x, double y) {
        pos = new Position(x, y);
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
    
    public void update() {
        this.pos.x += this.vx;
        this.pos.y += this.vy;
    }
    
    public void collide(Entity other) {
        //TODO: do something when they collide
    }

}
