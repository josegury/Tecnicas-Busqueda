package game;

/**
 * Hole elements in the field 
 * @author ssii
 *
 */

public class HolePost {
	
	Position  pos;
	int shape = 0; //0 is a square, it is here just for future versions (to use other shapes)
	
         HolePost (int xCoord,int yCoord){
		pos = new Position(xCoord,yCoord);
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	

}
