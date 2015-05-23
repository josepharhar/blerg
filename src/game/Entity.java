package game;

public interface Entity {
    
    /**
     * Get the x location of this entity
     */
    public double getx();
    
    /**
     * Get the y location of this entity
     */
    public double gety();
    
    /**
     * Get the radius of this entity, since all entities are circles
     */
    public double getr();
    
    /**
     * Tell this entity to update its position, etc.
     */
    public void update();
}
