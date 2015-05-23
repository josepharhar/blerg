package common;

public enum Direction {
	UP(Math.PI * 3 / 2),
	DOWN(Math.PI / 2),
	LEFT(Math.PI),
	RIGHT(0),
	DOWNLEFT(Math.PI * 3 / 4),
	DOWNRIGHT(Math.PI / 4),
	UPLEFT(Math.PI * 5 / 4),
	UPRIGHT(Math.PI * 7 / 4),
	HOLD(0);
	
	public double angle;
	
	private Direction(double angle) {
		this.angle = angle;
	}
}
