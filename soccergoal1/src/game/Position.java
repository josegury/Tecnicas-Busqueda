package game;

/**
 * A class representing a location on the soccer field, with x and y coordinates (integer numbers).
 * 
 * <br> When an element (barrier/goal) is in a {@link game.Position} <x,y> it covers the square from (x,y) to (x+1,y+1), 
 * that is, all real (double) numbers in between.
 * @author ssii
 *
 */
public class Position {
	
	/**
	  * x coordinate (integer numbers)
	  */
	int x;
	/**
	  * y coordinate (integer numbers)
	  */
  int y;
 

  

  /**
	 * @return the x
	 */
	public int getX() {
		return x;
	}




	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}




	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}




	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}




public boolean equals(Object o) {
    if (!(o instanceof Position))
      return false;
    Position l = (Position) o;
    if (l.x != x)
      return false;
    if (l.y != y)
      return false;
    return true;
  }
  
  

  
  public String toString() {
    return "("+x+","+y+")";
  }

  @Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + x;
	result = prime * result + y;
	return result;
}


public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
/**
 * 
 * @return obtains a transformation to an object of type {@link game.DoublePosition} 
 * where the coordinates <strong> realX </strong> and <strong> realY </strong> are 
 * the corresponding double values  of x and y. 
 * <br> For example, if x=1 and y=3 it will create an object {@link game.DoublePosition} with  {@link game.DoublePosition#realX} = 1.0 and
 * {@link game.DoublePosition#realY} = 3.0.
 */
  public DoublePosition toDoublePosition(){
	  return new DoublePosition((double)x,(double)y);
  }


  /**
   * 
   * @param a one object of class {@link game.Position}
   * @param b one object of class {@link game.Position}
   * @return Euclidean distance between a and b
   */
  public static double euclideanDistance(Position a, Position b) {
    // sqrt(x^2 + y^2)
    return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
  }

  /**
   * 
   * @param a one object of class {@link game.Position}
   * @param b one object of class {@link game.Position}
   * @return Manhattan distance between a and b
   */
  public static double manhattanDistance(Position a, Position b) {
    return Math.abs(a.getX()-b.getX())+Math.abs(a.getY()-b.getY());
  }
  
 
}