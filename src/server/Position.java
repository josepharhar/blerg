package server;

public class Position {
    public double x;
    public double y;
    
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public static double distance(Position one, Position two) {
        double dx = one.x - two.x;
        double dy = one.y - two.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
