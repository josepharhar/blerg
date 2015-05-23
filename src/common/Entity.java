package common;

import javafx.scene.paint.Color;

public class Entity {
    private long entityID;
    private double xLocation;
    private double yLocation;
    private double xVelocity;
    private double yVelocity;
    private double radius;
    private int red;
    private int blue;
    private int green;
    
    public long getEntityID() {
        return entityID;
    }
    
    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }
    
    public double getxLocation() {
        return xLocation;
    }
    
    public void setxLocation(double xLocation) {
        this.xLocation = xLocation;
    }
    
    public double getyLocation() {
        return yLocation;
    }
    
    public void setyLocation(double yLocation) {
        this.yLocation = yLocation;
    }
    
    public double getxVelocity() {
        return xVelocity;
    }
    
    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }
    
    public double getyVelocity() {
        return yVelocity;
    }
    
    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }
    
    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }
    
    
    public void setColor(Color color) {
        this.color = color;
    }
    
}
