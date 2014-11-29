package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class defines and controls all the necessary elements for playing a
 * SoccerGoal game.
 * 
 */

public class Game {

	/**
	 * field provides the main information about the current game configuration,
	 * that is: where goal is located where barriers are located where the agent
	 * is located, dimensions of the field.
	 */
	public Field field;

	/**
	 * agent indicates who (and of which type) is the agent playing the game.
	 */
	protected AgentPlayer agent;

	String lastError;

	/**
	 * This value will indicate how many milliseconds the GUI system will wait
	 * before running the next iteration, so that the human eye can perceive
	 * each state properly. So, smaller values will produce <i> faster </i>
	 * games, but also more difficult to follow. 50 seems to be a reasonable
	 * value for <i> normal speed </i> computers.
	 */
	protected int delayTime = 200; // the smaller the faster the game will be
									// played
	double playTimeInit;
	double playTimeEnd;
	double totalThinkTime = 0.0;
	double simulationCalls = 0.0;
	private double spaceUnit;

        public int holeCost = 1;

	public static double MAX_NUM_GENERATED_NODES = 150000;

	/**
	 * main window
	 */
	JFrame window;
	private boolean estela = true;
	private boolean debug = false;

	private int xDim;
	private int yDim;

	/**
	 * current iteration of the game
	 */
	public int currentIter = 0;
        public int holes= 0;

	/**
	 * maximum number of iterations before finalizing the game
	 */
	static final int maxIter = 4000;

	private GameStep gameStep;

	public GameStep getGameState() {
		return gameStep;
	}

	public AgentPlayer getAgent() {
		return agent;
	}

	public void setAgent(AgentPlayer agent) {
		this.agent = agent;
	}

	public static int getMaxiter() {
		return maxIter;
	}

	public double getSimulationCalls() {
		return simulationCalls;
	}

	public void setSimulationCalls(double simulationCalls) {
		this.simulationCalls = simulationCalls;
	}

	/**
	 * Constructor for {@link game.Game}
	 * 
	 * @param text
	 *            the window will be entitled with this sString preceded by SG
	 *            (from SoccerGoal)
	 * @param field
	 *            the soccer field (with all the elements)
	 * @param agent
	 *            the player that must implement the interface
	 *            {@link game.AgentPlayer}
	 * @param gui
	 *            true if GUI visualization, false if batch mode.
	 * 
	 */
	public Game(String text, Field field, AgentPlayer agent, boolean gui) {

		this.field = field;
		this.xDim = field.getXDim();
		this.yDim = field.getYDim();
		this.agent = agent;

		// if it is a Search Agent, the unit is given by it
		if (agent instanceof SearchAgent)
			this.spaceUnit = ((SearchAgent) agent).getUnit();

		window = new JFrame("SG " + text);

		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setFocusable(true);

		// Add the field info to the gui.
		window.getContentPane().add(field);

		window.pack();

		// if batch mode window will not be used
		if (gui)
			window.setVisible(true);
		else
			window.setVisible(false);

	}

	/**
	 * 
	 * @return true if game is ended (either successfully or faulty if the agent
	 *         used more iterations than permitted without reaching the goal).
	 * 
	 */
	public boolean isFinal() {
		boolean result = false;
		if (this.isInGoal()) {
			System.out.println("**************************************");
			System.out.println("Congratulations, you reached the goal!");
			System.out.println("Info:");
			System.out.println("- iterations = " + this.currentIter);
			System.out.println("- holes  = " + this.holes);
			System.out.println("- thinking time = "
					+ String.format("%.10f", this.totalThinkTime) + " sec.");
			System.out.println("--------------------------------------");
			System.out.println("Search Details:");
			System.out.println("Num of expanded nodes: "
					+ ((SearchAgent) this.agent).getNumExpandedNodes());
			System.out.println("Num of explored nodes: "
					+ ((SearchAgent) this.agent).getNumExploredNodes());
			System.out.println("Num of generated nodes: "
					+ ((SearchAgent) this.agent).getNumGeneratedNodes());
			System.out.println("--------------------------------------");
			System.out.println("Simulated nodes: " + simulationCalls);
			System.out.println("**************************************");
			GameStep current = null;

			if (gameStep != null && (gameStep.parent != null))
				current = gameStep.parent;
			if (current != null)
				if (estela) {
					while (current.parent != null) {
						current = current.parent;
					}
				}
			return true;

		} else if (this.currentIter > Game.maxIter) {
			System.out
					.println("Game ended because the agent exceeded the number of maximum iterations permitted.");
			return true;
		}
		return result;
	}

	/**
	 * 
	 * @param dp
	 * @return the center of mass of the agent (assuming, as always) diameter is
	 *         1.
	 */
	static public DoublePosition getAgentCenterOf(DoublePosition dp) {
		return new DoublePosition(dp.realX + 0.5, dp.realY + 0.5);
	}

	/**
	 * 
	 * @param cost
	 * @return Sets the cost associated to passing a hole
	 */
        
         public void setHoleCost(int cost)
         {
	     holeCost=cost;
	 }

    


	/**
	 * 
	 * @return true if the center of the agent (ball) is in any of the goal
	 *         positions
	 */

	public boolean isInGoal() {
		DoublePosition center = new DoublePosition(
				field.agentPosition.realX + 0.5,
				field.agentPosition.realY + 0.5);
		for (Position pos : field.getGoal()) {
			if (isContainedIn(center, pos))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @return true if being the agent in dp Position, its center is in any of
	 *         the goal positions
	 */
	public boolean isInGoal(DoublePosition dp) {
		DoublePosition center = new DoublePosition(dp.realX + 0.5,
				dp.realY + 0.5);
		for (Position pos : field.getGoal()) {
			if (isContainedIn(center, pos))
				return true;
		}
		return false;
	}


      
         public boolean isInHole(DoublePosition dp) {
    
        	DoublePosition centeredDP = new DoublePosition(dp.realX+0.5,dp.realY+0.5); 
  	        if(field.holes.get(centeredDP.toIntFloorPosition()) != null)
		    return true;
		else
		    return false;
	 }


	/**
	 * to check if a {@link game.DoublePosition} is contained in an
	 * <i>integer</i> {@link game.Position}
	 * 
	 */
	private boolean isContainedIn(DoublePosition dp, Position p) {

		if (between(p.x, p.x + 1, dp.realX) && between(p.y, p.y + 1, dp.realY))
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param move
	 *            the movement we want to check
	 * @param simulate
	 *            true if we are simulating, false if this is actual playing
	 * @return true if valid, false it is not
	 */
	private boolean isValidMove(Movement move, boolean simulate) {

		boolean valid = true;
		DoublePosition finalPos;
		DoublePosition initialPos;

		// get initial and final positions
		finalPos = move.getFinalPosition();
		initialPos = move.getInitialPosition();

		// this is in case the movement has not been really performed, do it now
		// to avoid null problems
		if (finalPos == null)
			finalPos = move.performMovement();

		// If the agent is in the goal, the movement is valid as long as there
		// is no collision entering. So, we do it before any bounds checking
		if (isInGoal(finalPos)) {
			if (this.testColision(initialPos.realX, initialPos.realY,
					finalPos.realX, finalPos.realY) == null) {
				simulationCalls++;
				return true;
			}
		}

		/*
		 * when working with real (double) numbers, Java can have small
		 * variations given numbers as -5.667788 E-16, it is almost 0, but in
		 * fact it is < 0. So we use round for tha Out of the limits --> move
		 * not valid (return false)
		 */
		if (Math.round(finalPos.realX) < 0
				|| Math.round(finalPos.realX) >= this.xDim
				|| Math.round(finalPos.realY) < 0
				|| Math.round(finalPos.realY) >= this.yDim) {
			lastError = new String(
					" move would place Agent OUTSIDE THE GAME FIELD\n ("
							+ finalPos.realX + "," + finalPos.realY
							+ ") --> out of [(0,0)(" + xDim + "," + yDim + ")]");
			return false;
		}

		/*
		 * round will produce 12 for 11.51, for example, or 0 for 0.49. The next
		 * two ifs avoid those situation. We only allow EPSILON as difference
		 * with the limits.
		 */
		if (((finalPos.realX < 0) && Math.abs(finalPos.realX) > DoublePosition.EPSILON)
				|| ((finalPos.realY < 0) && Math.abs(finalPos.realY) > DoublePosition.EPSILON)) {
			lastError = new String(
					" move would place Agent OUTSIDE THE GAME FIELD (just a bit but enough)\n ("
							+ finalPos.realX + "," + finalPos.realY
							+ ") --> out of [(0,0)(" + xDim + "," + yDim + ")]");
			return false;
		}

		if (((finalPos.realX > (this.xDim - 1)) && Math.abs(finalPos.realX
				- (double) this.xDim +1.0) > DoublePosition.EPSILON)
				|| ((finalPos.realY > (this.yDim - 1)) && Math
						.abs(finalPos.realY - (double) this.yDim +1.0) > DoublePosition.EPSILON)) {
			lastError = new String(
					" move would place Agent OUTSIDE THE GAME FIELD (just a bit but enough)\n ("
							+ finalPos.realX + "," + finalPos.realY
							+ ") --> out of [(0,0)(" + xDim + "," + yDim + ")]");
			return false;
		}

		// if the agent is within the field, just check if a collision happens,
		// if so move not valid
		Position collision = this.testColision(initialPos.realX,
				initialPos.realY, finalPos.realX, finalPos.realY);

		if (collision != null) {
			if (!simulate)
				lastError = new String("A collision was detected at  "
						+ collision);
			return false;
		}

		if (simulate)
			simulationCalls++;
		return valid;
	}

	/**
	 * 
	 * @param x
	 *            integer position for x-axis
	 * @param y
	 *            integer position for y-axis
	 * @return true if there is an obstacle in that coordinate (x,y)
	 */
	private boolean searchBarrier(int x, int y) {
		if (this.field.elements.get(new Position(x, y)) != null)
			return true;
		else
			return false;

	}


	private boolean searchHole(int x, int y) {
		if (this.field.holes.get(new Position(x, y)) != null)
			return true;
		else
			return false;

	}



	/**
	 * 
	 * @param x0
	 *            initial position for x-axis
	 * @param y0
	 *            initial position for y-axis
	 * @param xf
	 *            final position for x-axis
	 * @param yf
	 *            final position for y-axis
	 * @return null if no collision is detected when this movement is done, or a
	 *         point where a collision is found
	 */
	private Position testColision(double x0, double y0, double xf, double yf) {
		boolean debug = false;
		Position result = null;

		/*
		 * agent radius is 0.5, this takes a slightly smaller value so that the
		 * agent can pass close to them without colliding. If it is
		 * 0.499999999999 it will be no flexible at all, if it is 0.4 or smaller
		 * it would be too much flexible
		 */
		double radioPractico = 0.45;

		// center of the agent
		x0 = x0 + 0.5;
		y0 = y0 + 0.5;
		xf = xf + 0.5;
		yf = yf + 0.5;

		int i, j, k;
		double distPaso = Math.sqrt((xf - x0) * (xf - x0) + (yf - y0)
				* (yf - y0));

		// x and y values for the 5 reference points
		double[] doubleTesterX = new double[5];
		double[] doubleTesterY = new double[5];

		// crucial points to test
		double[] doubleTesterXRangosX = new double[4];
		double[] doubleTesterXRangosY = new double[4];

		// Parameters for the line that joins x0, y0 and xf, yf
		// y = a*xf + b
		double a, b;

		// Parameters for the orthogonal line that joins x0, y0 with xf, yf and
		// pass thorugh a generic point m
		// y = c*x + d
		double c, d;

		// Intersection point of both lines
		double xi, yi;

		// Distance between intersection and point m
		double dist;

		// Defining a rank in the represented world to search obstacles
		// x0rep and so on are the coordinates rank where we will look for them
		int x0rep = (int) xf - 1;
		int y0rep = (int) yf - 1;
		int xfrep = (int) xf + 1;
		int yfrep = (int) yf + 1;

		a = (y0 - yf) / (x0 - xf);
		b = y0 - a * x0;
		c = -(1 / a);

		if (debug) {
			System.out.println("Report:");
			System.out.println("Start trajectory : " + x0 + "," + y0);
			System.out.println("End trajectory   : " + xf + "," + yf);
			System.out.println("Distance: " + distPaso);
			System.out.println("Represented points, obstacle rank search: "
					+ x0rep + "," + y0rep + "," + xfrep + "," + yfrep + ",");
			System.out.println("Size of array for tested points: "
					+ doubleTesterX.length);
		}

		for (i = x0rep; i < xfrep + 1; i++) {
			for (j = y0rep; j < yfrep + 1; j++) {
				if (i >= 0 && j >= 0 && i < this.xDim && j < this.yDim
						&& this.searchBarrier(i, j)) {
					if (debug)
						System.out
								.println("Puntos que definen el obstaculo real: "
										+ i + "," + j);
					doubleTesterX[0] = (double) (i);
					doubleTesterX[1] = (double) (i);
					doubleTesterX[2] = (double) (i + 1);
					doubleTesterX[3] = (double) (i + 1);
					doubleTesterX[4] = (double) i + 0.5;
					doubleTesterY[0] = (double) (j);
					doubleTesterY[1] = (double) (j + 1);
					doubleTesterY[2] = (double) (j);
					doubleTesterY[3] = (double) (j + 1);
					doubleTesterY[4] = (double) j + 0.5;
					doubleTesterXRangosX[0] = (double) i - radioPractico;
					doubleTesterXRangosX[1] = (double) i + 1 + radioPractico;
					doubleTesterXRangosX[2] = (double) i;
					doubleTesterXRangosX[3] = (double) i + 1;
					doubleTesterXRangosY[0] = (double) j;
					doubleTesterXRangosY[1] = (double) j + 1;
					doubleTesterXRangosY[2] = (double) j - radioPractico;
					doubleTesterXRangosY[3] = (double) j + 1 + radioPractico;

					if (debug) {
						System.out
								.println("Coordinates of reference points for the obstacle");
						for (k = 0; k < 5; k++) {
							System.out.println(doubleTesterX[k] + ", "
									+ doubleTesterY[k]);
						}
						System.out
								.println("Coordinates of the orthogonal zone");
						for (k = 0; k < 4; k++) {
							System.out.println(doubleTesterXRangosX[k] + ", "
									+ doubleTesterXRangosY[k]);
						}
					}

					for (k = 0; k < 5; k++) {
						d = doubleTesterY[k] + doubleTesterX[k] / a;
						/*
						 * Point xi,yi is where trajectory and the orthogonal
						 * (thorugh the ref point) encounters To compute that:
						 * yi=a*xi+b, yi=c*xi+d ==> xi=(d-b)/(a-c)
						 */
						xi = (d - b) / (a - c);
						yi = a * xi + b;
						/*
						 * Check if (xi, yi) belongs to the segment determined
						 * by the agent movement
						 */
						if (between(x0, xf, xi) && between(y0, yf, yi)) {
							dist = Math.sqrt((doubleTesterX[k] - xi)
									* (doubleTesterX[k] - xi)
									+ (doubleTesterY[k] - yi)
									* (doubleTesterY[k] - yi));
							if (dist < radioPractico) {
								result = new Position(i, j);
								if (debug) {
									System.out
											.println("----> Found collision! ("
													+ doubleTesterX[k] + ","
													+ doubleTesterY[k] + ")");
									System.out
											.println("    --> Tested Interesection Point: "
													+ xi + ", " + yi);
									System.out.println("    --> Distance: "
											+ dist);
								}
							}
						}
						dist = Math.sqrt((doubleTesterX[k] - xf)
								* (doubleTesterX[k] - xf)
								+ (doubleTesterY[k] - yf)
								* (doubleTesterY[k] - yf));
						if (dist < radioPractico) {
							result = new Position(i, j);
							if (debug) {
								System.out
										.println("----> Found collision (final point)! ("
												+ doubleTesterX[k]
												+ ","
												+ doubleTesterY[k] + ")");
								System.out.println("    --> Final point: " + xf
										+ ", " + yf);
								System.out.println("    --> Distance: " + dist);
							}
						}
					}
					if (between(doubleTesterXRangosX[0],
							doubleTesterXRangosX[1], xf)
							&& between(doubleTesterXRangosY[0],
									doubleTesterXRangosY[1], yf)) {
						if (debug) {
							System.out.println("----> Found collision! (" + i
									+ "," + j + ")");
							System.out.println("    --> Rank "
									+ doubleTesterXRangosX[0] + ", "
									+ doubleTesterXRangosY[0]);
							System.out.println("    -->     and "
									+ doubleTesterXRangosX[1] + ", "
									+ doubleTesterXRangosY[1]);
						}

						result = new Position(i, j);
					}
					if (between(doubleTesterXRangosX[2],
							doubleTesterXRangosX[3], xf)
							&& between(doubleTesterXRangosY[2],
									doubleTesterXRangosY[3], yf)) {
						if (debug) {
							System.out.println("----> Found collision! (" + i
									+ "," + j + ")");
							System.out.println("    --> Rank "
									+ doubleTesterXRangosX[2] + ", "
									+ doubleTesterXRangosY[2]);
							System.out.println("    -->     and "
									+ doubleTesterXRangosX[3] + ", "
									+ doubleTesterXRangosY[3]);
						}

						result = new Position(i, j);
					}
				}
			}

		}
		// result will be null if no collision or a Position point
		return result;
	}

	private boolean between(double extremoRango1, double extremoRango2,
			double tester) {
		boolean result = false;
		if ((extremoRango1 <= tester) && (tester <= extremoRango2))
			result = true;
		if ((extremoRango1 >= tester) && (tester >= extremoRango2))
			result = true;
		return result;
	}

	/**
	 * 
	 * @param startingPos
	 *            initial position of the agent
	 * @param move
	 *            movement that wants to perform
	 * @return null if move is not valid, otherwise the final position
	 */
	public DoublePosition simulateMovement(DoublePosition startingPos,
			Movement move) {

		DoublePosition newPos = move.performMovement();
		DoublePosition returnedPos = null;
		if (move.getLinealSpace() > 1.0) {
			System.out
					.println("¡¡STOP!! Current version of the game doesn't guarantee correctness when ball performs movements larger than one spatial unit.");
			System.exit(-1);

		}

		if (isValidMove(move, true)) {
			if (debug)
				System.out.println("Agent simulates movement \n < " + move
						+ " > ");
			returnedPos = newPos;
		} else
			return null;
		return returnedPos;

	}

	/**
	 * @param startingPos
	 *            initial position of the agent
	 * @param angle
	 *            direction of movement that wants to perform, the unit has
	 *            already been set
	 * @return null if move is not valid, otherwise the final position
	 */

	public DoublePosition simulateMovement(DoublePosition startingPos,
			double angle) {

		Movement move = new Movement(startingPos, this.spaceUnit, angle);
		DoublePosition newPos = move.performMovement();
		DoublePosition returnedPos = null;
		if (move.getLinealSpace() > 1.0) {
			System.out
					.println("¡¡STOP!! Current version of the game doesn't guarantee correctness when ball performs movements larger than one spatial unit.");
			System.exit(-1);

		}

		if (isValidMove(move, true)) {
			if (debug)
				System.out.println("Agent simulates movement \n < " + move
						+ " > ");
			returnedPos = newPos;
		} else
			return null;
		return returnedPos;

	}

	/**
	 * 
	 * @param gui
	 *            true if GUI is used, false for batch mode
	 * @throws NoPathFoundException
	 *             it will mean that the agent didn't find the correct path
	 */
	public void play(boolean gui) throws NoPathFoundException {

		gameStep = new GameStep(field.agentPosition, null);
		Movement move = new Movement();
		GameStep newState;

		if (gui) {
			while (!isFinal()) {
				playTimeInit = System.currentTimeMillis();
				move = agent.move(this);
				playTimeEnd = System.currentTimeMillis();
				totalThinkTime += (playTimeEnd - playTimeInit) / 1000;

				currentIter++;
				DoublePosition newPos = move.performMovement();
				if (move.getLinealSpace() > 1.0) {
					System.out
							.println("¡¡STOP!! Current version of the game doesn't guarantee correctness when ball performs movements larger than one spatial unit.");
					System.exit(-1);

				}

				if (isValidMove(move, false)) {
					if (debug)
						System.out.println(String.format("%7d",
								this.currentIter)
								+ " -> Agent tries to do movement \n "
								+ "       < " + move + " > ");
					newState = new GameStep(newPos, gameStep);
					gameStep = newState;
					field.agentPosition = newPos;
					if(isInHole(newPos))
					{
					    System.err.println("Going through a hole \n");
					    holes++;
					}
					    
					    
					if (estela)
						field.gameState = this.gameStep;
					field.repaint();
					try {
						Thread.sleep(delayTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.err.println("You have tried to do an invalid move: "
							+ lastError);
					System.exit(-1);
				}
			}

		} else {
			// same game, but no paint, repaint and no delays
			while (!isFinal()) {
				playTimeInit = System.currentTimeMillis();
				move = agent.move(this);
				playTimeEnd = System.currentTimeMillis();
				totalThinkTime += (playTimeEnd - playTimeInit) / 1000;

				currentIter++;
				DoublePosition newPos = move.performMovement();
				if (move.getLinealSpace() > 1.0) {
					System.out
							.println("¡¡STOP!! Current version of the game doesn't guarantee correctness when ball performs movements larger than one spatial unit.");
					System.exit(-1);

				}

				if (isValidMove(move, false)) {
					if (debug)
						System.out.println(String.format("%7d",
								this.currentIter)
								+ " -> Agent tries to do movement \n "
								+ "       < " + move + " > ");
					newState = new GameStep(newPos, gameStep);
					gameStep = newState;
					field.agentPosition = newPos;
					if(isInHole(newPos))
					{
					    System.err.println("Going through a hole \n");
					    holes++;
					}

				} else {
					System.err.println("You have tried to do an invalid move: "
							+ lastError);
					System.exit(-1);
				}
			}

		}

		if (this.currentIter > Game.maxIter)
			throw new NoPathFoundException("No path found, too many iterations");

	}

	/*
	 * finish the game
	 */
	public void pullThePlug() {
		WindowEvent wev = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}

}
