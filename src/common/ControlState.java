package common;

public class ControlState {
	private boolean splitting;
	private boolean shooting;
	private double direction;
	
	//1 or greater = max speed
	private double magniture;

    public boolean isSplitting() {
        return splitting;
    }

    public void setSplitting(boolean splitting) {
        this.splitting = splitting;
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getMagniture() {
        return magniture;
    }

    public void setMagniture(double magniture) {
        this.magniture = magniture;
    }
}
