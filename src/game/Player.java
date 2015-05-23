package game;

public class Player implements Entity {
    
    private double x;
    private double y;
    private double r;
    
    private double vx;
    private double vy;
    
    public double getx() {
        return x;
    }
    
    public double gety() {
        return y;
    }
    
    public double getr() {
        return r;
    }
    
    public void update() {
        this.x += this.vx;
        this.y += this.vy;
    }

}
