package common;

import org.codehaus.jackson.annotate.JsonIgnore;

import javafx.scene.paint.Color;


public class Entity {
    
    public static enum EntityType {
        PLAYER,
        FOOD,
        VIRUS;
    }
    
    private long entityID;
    
    protected double xLocation;
    protected double yLocation;
    protected double xVelocity;
    protected double yVelocity;
    protected double radius;
    
    private int red;
    private int blue;
    private int green;
    private EntityType type;
    
    public Entity(long entityID, double xLocation, double yLocation,
            double xVelocity, double yVelocity, double radius, int red,
            int blue, int green, EntityType type) {
        this.entityID = entityID;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.radius = radius;
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.type = type;
    }
    
    public Entity() {
        
    }
    
    public void update() {
        this.xLocation += xVelocity;
        this.yLocation += yVelocity;
    }
    
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

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }
    
    @JsonIgnore
    public Color getColor() {
        return Color.rgb(red, green, blue);
    }
}
