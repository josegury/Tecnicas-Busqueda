package search;

import game.*;


/**
 * This agent just read the solution from an array. So we must be sure we have the correct game and the correct solution, or
 * errors will probably happen.
 * @author ssii
 */
public class ReadArrayAgent extends SearchAgent {

	
	public int[] myPath = {1,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,2,1,2,1,2,2,1,0,1,0,0,0,0,1,1,0,0,1};
        //This exact path works ok when playing -type 1 scenary


	int step = -1;
	
	public ReadArrayAgent(int num) {
		super(num);
	}


	public int nextMove(Game game) {
		step++;
		if (step==0)
		{
		this.setNumExpandedNodes(0.0);
		this.setNumExploredNodes(0.0);
		this.setNumGeneratedNodes(0.0);
		}
		return myPath[step];
		
	}

	
}
