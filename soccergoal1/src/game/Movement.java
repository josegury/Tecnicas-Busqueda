package game;

/**
 * This class will manage the Movements in a soccer goal game
 * @author ssii
 * 
 */
public class Movement {

	private DoublePosition initialPosition;
	private DoublePosition finalPosition; // This is computed from the other values
											
	private double constantVelocity = 1;
	private double movementTime;
	private double direction; // radian angle for direction
	private double xIncr;
	private double yIncr;
	private double linealSpace;

	public DoublePosition getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(DoublePosition initialPosition) {
		this.initialPosition = initialPosition;
	}

	public DoublePosition getFinalPosition() {
		return finalPosition;
	}

	public double getConstantVelocity() {
		return constantVelocity;
	}

	public void setConstantVelocity(double constantVelocity) {
		this.constantVelocity = constantVelocity;
	}

	public double getLinealSpace() {
		return linealSpace;
	}

	public void setLinealSpace(double linealSpace) {
		this.linealSpace = linealSpace;
	}

	public Movement() {
	}

	public Movement(DoublePosition initialPosition, double time, double angle) {

		this.initialPosition = initialPosition;
		this.movementTime = time;
		this.direction = angle;
		this.linealSpace = this.constantVelocity * this.movementTime;

	}

	public DoublePosition performMovement() {

		this.xIncr = this.linealSpace * Math.cos(this.direction);
		this.yIncr = this.linealSpace * Math.sin(this.direction);

		this.finalPosition = new DoublePosition(
				(this.initialPosition.getRealX() + xIncr),
				(this.initialPosition.getRealY() - yIncr));
		return finalPosition;

	}

	@Override
	public String toString() {
		return "Movement [initialPosition=" + initialPosition
				+ ", finalPosition=" + finalPosition + ", direction="
				+ String.format("%.5f", direction) + " (=="
				+ Math.round(Math.toDegrees(direction)) + " grad )"
				+ ", xIncr=" + String.format("%.2f", xIncr) + ", yIncr="
				+ String.format("%.2f", yIncr) + " , linealSpace="
				+ String.format("%.4f", linealSpace) + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(constantVelocity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(direction);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((finalPosition == null) ? 0 : finalPosition.hashCode());
		result = prime * result
				+ ((initialPosition == null) ? 0 : initialPosition.hashCode());
		temp = Double.doubleToLongBits(linealSpace);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(movementTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xIncr);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yIncr);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movement other = (Movement) obj;
		if (Double.doubleToLongBits(constantVelocity) != Double
				.doubleToLongBits(other.constantVelocity))
			return false;
		if (Double.doubleToLongBits(direction) != Double
				.doubleToLongBits(other.direction))
			return false;
		if (finalPosition == null) {
			if (other.finalPosition != null)
				return false;
		} else if (!finalPosition.equals(other.finalPosition))
			return false;
		if (initialPosition == null) {
			if (other.initialPosition != null)
				return false;
		} else if (!initialPosition.equals(other.initialPosition))
			return false;
		if (Double.doubleToLongBits(linealSpace) != Double
				.doubleToLongBits(other.linealSpace))
			return false;
		if (Double.doubleToLongBits(movementTime) != Double
				.doubleToLongBits(other.movementTime))
			return false;
		if (Double.doubleToLongBits(xIncr) != Double
				.doubleToLongBits(other.xIncr))
			return false;
		if (Double.doubleToLongBits(yIncr) != Double
				.doubleToLongBits(other.yIncr))
			return false;
		return true;
	}
	
}