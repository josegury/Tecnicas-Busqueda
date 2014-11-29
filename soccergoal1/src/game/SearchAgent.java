package game;

import java.util.ArrayList;

/**
 * This is an asbtract class, all Search agents will inherit from it, it
 * implements interface {@link game.AgentPlayer}
 * 
 * @author ssii
 * 
 */

public abstract class SearchAgent implements AgentPlayer {

	/**
	 * array containing all possible angles for this agent movements
	 */
	public double[] possibleAngles;

	/**
	 * agent will have to indicate which is the chosen index, within
	 * {@link #possibleAngles} array, initially set to -1.
	 */
	int posChosen = -1;

	/**
	 * the solution path will be saved here
	 */
	ArrayList<Integer> pathFound = new ArrayList<Integer>();

	/**
	 * this indicate the length od the steps given by agent movements
	 */
	public double unit = 1;

	/**
	 * this value indicates how many divisions of one square unit are done in
	 * every dimension, so that one square unit will yield discrN x discrN
	 * possible states, if they are reachable.
	 */
	public static final int discrN = 40;

	/**
	 * this is in fact, the length of {@link #possibleAngles}, it will be needed
	 * to indicate this number in the constructor when creating a Search agent
	 */
	public int numAngles;

	/**
	 * number of expanded nodes
	 */
	protected double numExpandedNodes = -1;

	/**
	 * number of explored nodes
	 */
	protected double numExploredNodes = -1;

	/**
	 * number of generated nodes
	 */
	protected double numGeneratedNodes = -1;

	/**
	 * 
	 * @param num
	 *            number of angles, distributed uniformly from 0 to 2*pi. <br>
	 *            see images in <a
	 *            href="https://www.dropbox.com/sh/yl5qu0glzicwxg7/Xig-wZE0u2"
	 *            >url link</a> for more detail
	 */
	public SearchAgent(int num) {
		numAngles = num;
		possibleAngles = new double[numAngles];
		for (int i = 0; i < numAngles; i++)
			possibleAngles[i] = (Math.PI * 2.0) * ((double) i / numAngles);
	}

	/**
	 * IMPORTANT --> all methods that extend Search Agent must implement
	 * nextMove()
	 */
	public abstract int nextMove(Game game);

	/**
	 * @return the movement that correspond from the index return by the
	 *         subclass, and handles NoFoundException if this index is -1
	 */
	public Movement move(Game game) throws NoPathFoundException {

		posChosen = this.nextMove(game);
		if (posChosen == -1)
			throw new NoPathFoundException("No Path Found Using Search");
		Movement myMove = new Movement(game.field.agentPosition, unit,
				possibleAngles[posChosen]);
		pathFound.add(posChosen);
		return myMove;
	}

	/**
	 * prints the path
	 */
	public void printPathFound() {

		if (pathFound.size() == 0)
			System.out.println("{empty_path}");
		else {

			System.out.print("{");
			int k;
			for (k = 0; k < (this.pathFound.size() - 1); k++)
				System.out.print(this.pathFound.get(k) + ",");
			System.out.println(this.pathFound.get(this.pathFound.size() - 1)
					+ "}");
		}

	}

	/**
	 * 
	 * @param value
	 *            the real position of the agent
	 * @return a discretized state
	 */
	public static DoublePosition getNDiscrStateFor(DoublePosition value) {
		double realX = ((int) Math.round(value.getRealX() * (double) discrN))
				/ (double) discrN;
		double realY = ((int) Math.round(value.getRealY() * (double) discrN))
				/ (double) discrN;
		DoublePosition reprDiscrState = new DoublePosition(realX, realY);
		return reprDiscrState;

	}

	public double getNumExpandedNodes() {
		return numExpandedNodes;
	}

	public void setNumExpandedNodes(double numExploredNodes) {
		this.numExpandedNodes = numExploredNodes;
	}

	public double getNumExploredNodes() {
		return numExploredNodes;
	}

	public void setNumExploredNodes(double numExploredNodes) {
		this.numExploredNodes = numExploredNodes;
	}

	public double getNumGeneratedNodes() {
		return numGeneratedNodes;
	}

	public void setNumGeneratedNodes(double numGeneratedNodes) {
		this.numGeneratedNodes = numGeneratedNodes;
	}

	public static int getDiscrN() {
		return discrN;
	}

	public double getUnit() {
		return unit;
	}

	public void setUnit(double unit) {
		this.unit = unit;
	}

}
