	package search;
	
	import game.*;
	
	/**
	 * Greedy Agent that extends superclass {@link game.SearchAgent}, but it
	 * <strong> doesn't behave as a search algorithm </strong>. At each game
	 * iteration/step it chooses the movement that places the ball closest to the
	 * mid-goal, in format {@link game.Position}. This agent will not solve the game
	 * if it enters in cycles.
	 * 
	 * @author ssii
	 * 
	 */
	public class GreedyAgentNaive extends SearchAgent {
	
		boolean debug = false;
	
		/**
		 * Constructor.
		 * 
		 * @param num
		 *            receives the number of angles to be used, that will process
		 *            the superclass SearchAgent.
		 */
		public GreedyAgentNaive(int num) {
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
	
			DoublePosition iPosition;
	
			double[] distances = new double[num];
	
			// loop for any hypothetical possible action
			for (i = 0; i < num; i++) {
	
				// the game simulates what happens if this agent moves from the
				// current agent position using angle of index i
				iPosition = game.simulateMovement(game.field.agentPosition,
						possibleAngles[i]);
	
				/* if previously simulateMovement returns null means the this movement is not valid, otherwise iPosition will
				   contain the DoublePosition that correspond to the final position, given that the movement would have been performed */
				if (iPosition != null) {
	
					// iPosition is the final position of a valid Movement, so we
					// compute the distance to midGoal
					distances[i] = DoublePosition.euclideanDistance(iPosition,
							game.field.getMidGoal().toDoublePosition());
					// Added to include the cost of going through a hole 
					if(game.isInHole(iPosition))
					     distances[i]+=game.holeCost;
	
					if (debug) {
						System.out
								.println("Distance of moving in direction-angle i="
										+ i + " is " + distances[i]);
					}
				} else
					// we put in distance[i] a very large value ('emulating'
					// infinite)
					distances[i] = Double.MAX_VALUE;
	
			}
	
			/* Code for computing the index of minimum distance */
	
			double min = Double.MAX_VALUE;
			int minInd = -1;
	
			for (i = 0; i < num; i++) {
				if (distances[i] < min) {
					min = distances[i];
					minInd = i;
				}
	
			}
	
		
			if (debug)
				System.out.println("I chose i=" + minInd + "!!!");
	
			// minInd is now the chosen index
			return minInd;
		}
	
	}
