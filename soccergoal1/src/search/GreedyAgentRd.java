package search;

import game.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Modification of {@link search.GreedyAgentNaive} to allow problems when
 * cycling. If the agent chooses a {@link DoublePosition} already visited, then
 * it omits the greedy behaviour (to avoid cycles) and chooses any random valid
 * movement.
 * 
 * @author ssii
 * 
 */
public class GreedyAgentRd extends SearchAgent {

	boolean debug = false;
	//List of visited positions (states)
	HashSet<DoublePosition> visited = new HashSet<DoublePosition>();
	int seed = 9;
	Random rd = new Random(seed);

	//Initially no positions have been tested
	double generatedNum = 0;
	double expandedNum = 0;
	double exploredNum = 0;

	int step = -1;

	/**
	 * Constructor.
	 * 
	 * @param num receives the number of angles to be used, that will process
	 *            the superclass SearchAgent.
	 */
	public GreedyAgentRd(int num) {
		super(num);
	}

	
	/**
	 * 
	 * @return the index of the angle in which direction the agent chooses to
	 *         play at this step. This index is the position of the angle, in
	 *         radians in array {@link game.SearchAgent#possibleAngles}
	 * 
	 */
	public int nextMove(Game game) {
		int i;
		
		// num will keep the maximum number of possible angles
		int num = possibleAngles.length;
		
		Movement iMove = new Movement();
		DoublePosition iPosition;
		ArrayList<Integer> valid = new ArrayList<Integer>();

		double[] distances = new double[num];

		step++;

		/*In case that current step is greater than the number of iterations the game allows,
		it has no sense lloking for a move and, besides, I need to write the statistics so that
		they can be returned to the game */ 
		
		if (step == Game.getMaxiter()) {
			System.out.println(":( I cannot finish on time!!");
			setNumExpandedNodes(expandedNum);
			setNumExploredNodes(exploredNum);
			setNumGeneratedNodes(generatedNum);
			return -1;
		}

		// loop for any hypothetical possible action
		for (i = 0; i < num; i++) {

			//iMove will save the corresponding movement for action (angle) of index i
			iMove = new Movement(game.field.agentPosition, unit,
					possibleAngles[i]);

			//iPosition will be the final position if this iMove is performed, if null, iMove is not valid
			iPosition = game.simulateMovement(game.field.agentPosition, iMove);

			if (iPosition != null) {
                //if the movement is valid, we are trying one position, so this is a generated one
				generatedNum++;

				//we add it to the valid vector, just in case we need a random selection later
				valid.add(i);

				
				//compute the distance to midGoal of iPosition
				distances[i] = DoublePosition.euclideanDistance(iPosition,
						game.field.getMidGoal().toDoublePosition());

				// Added to include the cost of going through a hole 
				if(game.isInHole(iPosition)) distances[i]+=game.holeCost;				
				
				if (debug) {
					System.out
							.println("Distance of moving in direction-angle i="
									+ i + " is " + distances[i]);
				}
				
			} else //a very large value is set
				distances[i] = Double.MAX_VALUE;

		}
		//all the possible successors have been explored, so both explored and expanded are incremented
		expandedNum++;
		exploredNum++;

		double min = Double.MAX_VALUE;
		int minInd = -1;

		for (i = 0; i < num; i++) {
			if (distances[i] < min) {
				min = distances[i];
				minInd = i;
			}
		}

		if (minInd == -1) {
			setNumExpandedNodes(expandedNum);
			setNumExploredNodes(exploredNum);
			setNumGeneratedNodes(generatedNum);
			return -1;
		}

		iMove = new Movement(game.field.agentPosition, unit,
				possibleAngles[minInd]);
		iPosition = iMove.performMovement();

		DoublePosition stateIPosition = SearchAgent
				.getNDiscrStateFor(iPosition);

		Integer minInd2 = null;

		if (!visited.contains(stateIPosition))
			visited.add(stateIPosition);
		else
			minInd2 = chooseRandomValid(valid);

		if (debug)
			System.out.println("I chose i=" + minInd + " and random index (minInd2) is "+ minInd2 + "!!!");

		//if minInd2 is null it is because the current position was not already visited
		if (minInd2 == null) {
			if (game.isInGoal(iPosition)) {
				exploredNum++;
				generatedNum++;
				setNumExpandedNodes(expandedNum);
				setNumExploredNodes(exploredNum);
				setNumGeneratedNodes(generatedNum);
				return minInd;
			}
		} else {
            //we perform the corresponding movement for the random index minInd2
			iMove = new Movement(game.field.agentPosition, unit,
					possibleAngles[minInd2]);
			iPosition = iMove.performMovement();
			if (game.isInGoal(iPosition)) {
				exploredNum++;
				generatedNum++;
				setNumExpandedNodes(expandedNum);
				setNumExploredNodes(exploredNum);
				setNumGeneratedNodes(generatedNum);
				return minInd2;
			}
		}

		if (minInd2 == null)
			return minInd;
		else
			return minInd2;
	}

	/**
	 * 
	 * This method receives a list of indices
	 * @param valid the list
	 * @return a random value of the list
	 */
	int chooseRandomValid(ArrayList<Integer> valid) {

		return valid.get(rd.nextInt(valid.size()));
	}

}
