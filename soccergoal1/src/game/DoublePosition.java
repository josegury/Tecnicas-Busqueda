package game;

/**
 * A class representing a location on the soccer field, with x and y coordinates (double numbers).
 * @author ssii
 *
 */
public class DoublePosition {
	
 /**
  * x coordinate (real numbers)
  */
  public double realX;
  /**
   * y coordinate (real numbers)
   */
  public double realY;
  /**
   * This value is used to determined via {@link #equals(Object)} if the double values of x and y are <i> almost </i> and enough equal.
   * For more strict equalities, {@link #EPSILON} must be smaller, and for more flexible comparisons it should be greater. We have determined is as {@value #EPSILON}.
   */
  public static final double EPSILON = 0.000001;
  
  







  /**
 * @return the realX
 */
public double getRealX() {
	return realX;
}


/**
 * @param realX the realX to set
 */
public void setRealX(double realX) {
	this.realX = realX;
}


/**
 * @return the realY
 */
public double getRealY() {
	return realY;
}


/**
 * @param realY the realY to set
 */
public void setRealY(double realY) {
	this.realY = realY;
}


/**
 * @return the epsilon
 */
public static double getEpsilon() {
	return EPSILON;
}


public boolean equals(Object o) {
    if (!(o instanceof DoublePosition))
      return false;
    DoublePosition l = (DoublePosition) o;
    if (Math.abs(l.realX - realX)> EPSILON)
      return false;
    if (Math.abs(l.realY - realY)> EPSILON)
      return false;
    return true;
  }

  
  public String toString() {
	double xIsZero = realX;
	double yIsZero = realY;
	
	if (realX>0 && realY>0) 
	    return String.format("(%.2f-%.2f)", realX,realY);
	
	if (realX<0 && Math.abs(realX) < DoublePosition.EPSILON) xIsZero=0.0;
	if (realY<0 && Math.abs(realY) < DoublePosition.EPSILON) yIsZero=0.0;
	return String.format("(%.2f-%.2f)", xIsZero,yIsZero);
			
	
  }

  @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	long temp;
	temp = Double.doubleToLongBits(realX);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	temp = Double.doubleToLongBits(realY);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	return result;
}





public DoublePosition(double realX, double realY) {
    this.realX = realX;
    this.realY = realY;
  }
  
/**
 * 
 * @return obtains a transformation to an object of type {@link game.Position} 
 * where the coordinates <strong> x </strong> and <strong> y </strong> are 
 * the largest previous integer values (floor)  of realX and realY. 
 */
  public Position toIntFloorPosition(){
	  return new Position((int)Math.floor(realX),(int)Math.floor(realY));
  }
  
  /**
   * 
   * @return obtains a transformation to an object of type {@link game.Position} 
   * where the coordinates <strong> x </strong> and <strong> y <\strong> are 
   * the smallest following integer values (ceiling) of realX and realY. 
   */
  public Position toIntCeilingPosition(){
	  return new Position((int)Math.ceil(realX),(int)Math.ceil(realY));
  }
  
  /**
   * 
   * @return obtains a transformation to an object of type {@link game.Position} 
   * where the coordinates <strong> x </strong> and <strong> y <\strong> are 
   * the nearest integer values (round) of realX and realY. 
   */
  public Position toIntRoundPosition(){
	  return new Position((int)Math.round(realX),(int)Math.round(realY));
  }
  
  /**
   * 
   * @param a one object of class {@link game.DoublePosition}
   * @param b one object of class {@link game.DoublePosition}
   * @return Euclidean distance between a and b
   */

  public static double euclideanDistance(DoublePosition a, DoublePosition b) {
    // sqrt(x^2 + y^2)
    return Math.sqrt((a.getRealX()-b.getRealX())*(a.getRealX()-b.getRealX())+(a.getRealY()-b.getRealY())*(a.getRealY()-b.getRealY()));
  }

  /**
   * 
   * @param a one object of class {@link game.DoublePosition}
   * @param b one object of class {@link game.DoublePosition}
   * @return Manhattan distance between a and b
   */
  public static double manhattanDistance(DoublePosition a, DoublePosition b) {
    return Math.abs(a.getRealX()-b.getRealX())+Math.abs(a.getRealY()-b.getRealY());
  }
  

}