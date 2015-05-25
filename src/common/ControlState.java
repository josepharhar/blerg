package common;

public class ControlState {
    private volatile boolean splitting;
    private volatile boolean shooting;
    private volatile double direction;

    // 1 or greater = max speed
    private double magnitude;

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

    public double getMagnitude() {
        return magnitude > 1.0 ? 1.0 : (magnitude > 0 ? magnitude : 0);
    }

    public void setMagnitude(double magniture) {
        this.magnitude = magniture;
    }

    @Override
    public String toString() {
        return "ControlState [splitting=" + splitting + ", shooting="
                + shooting + ", direction=" + direction + ", magniture="
                + magnitude + "]";
    }
}
