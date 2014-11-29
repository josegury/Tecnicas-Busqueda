package game;

/**
 * Barrier elements in the field (GUI will show them in red squares)
 * @author ssii
 *
 */

public class BarrierPost {
	
	Position  pos;
	int shape = 0; //0 is a square, it is here just for future versions (to use other shapes)
	
	BarrierPost (int xCoord,int yCoord){
		pos = new Position(xCoord,yCoord);
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	

}
