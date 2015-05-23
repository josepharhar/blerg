package game;

import collision.Position;
import javafx.scene.paint.Color;

public interface Entity {
	
	public long getEntityID();
	
	public Color getColor();
    
    /**
     * Get the x location of this entity
     */
    public double getx();
    
    /**
     * Get the y location of this entity
     */
    public double gety();
    
    /**
     * Get position (x and y)
     */
    public Position getLocation();
    
    /**
     * Get the radius of this entity, since all entities are circles
     */
    public double getr();
    
    /**
     * Tell this entity to update its position, etc.
     */
    public void update();
    
    /**
     * Tell this entity to collide with another entity
     */
    public void collide(Entity other);
}
