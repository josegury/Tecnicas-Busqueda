package game;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import search.*;

/**
 * Main class for playing Soccer Game, run {@link #printUse()} to know the possibilities.
 * @author ssii
 *
 */
public class Main {

	final static long pauseBetweenGames = 1200L;

	String[] arguments = { "-type", "-practica", "-batch", "-agent", "-seed",
			"-xDim", "-yDim", "-numAngles" };

	/**
	 * 
	 * @param className
	 *            the name of the class, for example "search.GreedyAgentNaive"
	 * @param arg
	 *            it must be an integer number (corresponds to numAngles)
	 * @return a new SearchAgent object of class className and numAngles = arg
	 */
	public static Object getNewSearchAgentByName(String className, int arg) {
		Object o = null;
		try {
			Class classDefinition = Class.forName(className);
			Class[] intArgsClass = new Class[] { int.class };
			Constructor con = classDefinition.getConstructor(intArgsClass);
			Integer ARG = new Integer(arg);
			Object[] intArgs = new Object[] { ARG };
			o = con.newInstance(intArgs);
		} catch (Exception e) {
			System.err.println("Class " + className + " not found");
			System.exit(-1);

		}
		return o;
	}

	/**
	 * A simpler form of command line argument parsing. Parses command line
	 * arguments into a Map. Arguments of the form -flag1 arg1 -flag2 -flag3
	 * arg3 will be parsed so that the flag is a key in the Map (including the
	 * hyphen) and the optional argument will be its value (if present). In this
	 * version, if the argument is numeric, it will be a Double value in the
	 * map, not a String.
	 * 
	 * @param args
	 * @return A Map from keys to possible values (String or null)
	 */
	public static Map<String, Object> parseCommandLineArguments(String[] args,
			boolean parseNumbers) {
		Map<String, Object> result = new HashMap<String, Object>();
		String key, value;
		for (int i = 0; i < args.length; i++) {
			key = args[i];
			if (key.charAt(0) == '-') {
				if (i + 1 < args.length) {
					value = args[i + 1];
					if (value.charAt(0) != '-') {
						if (parseNumbers) {
							Object numericValue = value;
							try {
								numericValue = Double.parseDouble(value);
							} catch (NumberFormatException e2) {
							}
							result.put(key, numericValue);
						} else {
							result.put(key, value);
						}
						i++;
					} else {
						result.put(key, null);
					}
				} else {
					result.put(key, null);
				}
			}
		}
		return result;
	}

	/**
	 * to indicate the correct use of game.Main
	 */

	public static void printUse() {

		System.out
				.println("java game.Main [arguments]\n\nPossible arguments: \n-------------------------------------------------------------------------------------------\n");
		System.out
				.println("   -agent agent_file. Use the .class specified as search agent\n   (it must extend SearchAgent and it must be in package search, so: search.FileName)");
		System.out
				.println("   -numAngles num. It indicates how many possible angles the agent can use for movements\n   (if not specified, default = 4)");

		System.out
				.println("   -batch. It doesn't use GUI. Results are shown only in text output. (default = GUI mode)");
		System.out
				.println("   -seed number. Use number as a fixed seed in order to keep the same fields\n   in every program run (for debugging, professors will use any seed when assessment).");

		System.out
				.println("   -unit number. number must be positive and equal or less than 1.0 (default = 1.0)");
		System.out
				.println("     NOTE: -unit option has no effect in 'practica' mode");

		System.out
				.println("\n   Also, include ONLY ONE of the three following options:\n");

		System.out
				.println("   \t -type value. There are four simple predefined fields for debugging (see assignment). \n   \t value must be in {1,2,3,4,5} ");
		System.out
				.println("   \t -xDim numX and/or -yDim numY. \n   \t These are also for debugging purposes, numX and numY must be integer numbers.");
		System.out
				.println("   \t (If only one dimesion is specified, the field will be square, \n   \t taking the same length for both dimensions)");
		System.out
				.println("   \t -practica.  This will show a sequence of fields that must be all solved by the agent \n   \t (or indicating there was no found solution), in increasing complexity.");
		System.out
				.println("   \t (IMPORTANT: Students will be assessed using this 'practica' mode.)");
		System.out
				.println("------------------------------------------------------------------------------------------\n");

	}

	/**
	 * to print the stats for a game abruptly finished
	 */
	static public void printStats(Game sg, AgentPlayer agent) {
		System.out.println("!!!!!!!! Nodes stats until no path !!!!!!!!!");
		System.out.println("!!! - iterations = " + sg.currentIter);
		System.out.println("!!! - thinking time = "
				+ String.format("%.10f", sg.totalThinkTime) + " sec.");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("!!! Search Details:");
		System.out.println("!!! Num of expanded nodes: "
				+ ((SearchAgent) agent).getNumExpandedNodes());
		System.out.println("!!! Num of explored nodes: "
				+ ((SearchAgent) agent).getNumExploredNodes());
		System.out.println("!!! Num of generated nodes: "
				+ ((SearchAgent) agent).getNumGeneratedNodes());
		System.out.println("--------------------------------------");
		System.out.println("!!! Simulated nodes: " + sg.simulationCalls);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n");
	}

	// For Stats -- practica mode
	/**
	 * Return sum of all values in array.
	 */
	public static double sum(double[] a, boolean[] use) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			if (use[i])
				sum += a[i];
		}
		return sum;
	}

	public static int length(double[] a, boolean[] use) {
		int l = 0;
		for (int i = 0; i < a.length; i++) {
			if (use[i])
				l++;
		}
		return l;

	}

	/**
	 * Return average value in array, NaN if no such value.
	 */
	public static double mean(double[] a, boolean[] use) {
		int len = length(a, use);
		if (len == 0)
			return Double.NaN;
		double sum = sum(a, use);
		return sum / len;
	}

	/**
	 * Return sample variance of array, NaN if no such value.
	 */
	public static double var(double[] a, boolean[] use) {
		int len = length(a, use);
		if (len == 0)
			return Double.NaN;
		double avg = mean(a, use);
		double sum = 0.0;
		for (int i = 0; i < len; i++) {
			if (use[i])
				sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (len - 1);
	}

	/**
	 * Return sample standard deviation of array, NaN if no such value.
	 */
	public static double stddev(double[] a, boolean[] use) {
		return Math.sqrt(var(a, use));
	}

	/**
	 * Return average value in array, NaN if no such value.
	 */
	public static double mean(double[] a) {
		if (a.length == 0)
			return Double.NaN;
		double sum = sum(a);
		return sum / a.length;
	}

	/**
	 * Return sum of all values in array.
	 */
	public static double sum(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	// End Stats

	public static void main(String args[]) {

		Map<String, Object> argMap = Main.parseCommandLineArguments(args, true);
		Integer seed = null;
		int type = -2;
		int xDim = -1, yDim = -1;
		boolean gui = true;
		String agentType = new String("");
		int angles = 4;
		boolean practica = false;
		boolean dimensions = false;
		boolean fieldInText = false;
		double unit = 1.0;
		Random rd = new Random();

		// Practica mode keeps stats
		double[] genNodes = new double[15];
		double[] explNodes = new double[15];
		double[] expandNodes = new double[15];
		double[] simCalls = new double[15];
		double[] time = new double[15];
		double[] iter = new double[15];
		double[] cost = new double[15];
		double[] holes = new double[15];
		boolean[] solved = new boolean[15];
		boolean[] vnotSolved = new boolean[15];
		for (int k = 0; k < solved.length; k++) {
			solved[k] = true;
			vnotSolved[k] = false;
		}

		if (argMap.isEmpty()) {
			Main.printUse();
			System.exit(-1);
		}

		if (argMap.containsKey("-seed")) {
			seed = ((Double) argMap.get("-seed")).intValue();
		} else
			seed = null;

		if (argMap.containsKey("-unit")) {
			unit = ((Double) argMap.get("-unit")).doubleValue();
			if (argMap.containsKey("-practica"))
				System.out
						.println("-unit option has no effect in 'practica' mode");
			if (unit <= 0.0 || unit > 1.0) {
				System.out
						.println("Wrong value for unit, automatically set to 1.0");
				unit = 1.0;
			}
		}

		if (argMap.containsKey("-type") && argMap.containsKey("-practica")) {
			System.err
					.println("\n '-type' and '-practica' cannot be used simultaneously, they are contradictory. Please, choose only one of these options.");
			System.exit(-1);
		}

		if ((argMap.containsKey("-xDim") || argMap.containsKey("-yDim"))
				&& argMap.containsKey("-practica")) {
			System.err
					.println("\n '-xDim' and/or '-yDim' cannot be used simultaneously with '-practica', they are contradictory. Please, choose only one of these options.");
			System.exit(-1);
		}

		if ((argMap.containsKey("-xDim") || argMap.containsKey("-yDim"))
				&& argMap.containsKey("-type")) {
			System.err
					.println("\n '-xDim' and/or '-yDim' cannot be used simultaneously with '-type', they are contradictory. Please, choose only one of these options.");
			System.exit(-1);
		}

		if (argMap.containsKey("-xDim") && argMap.containsKey("-yDim")) {
			xDim = ((Double) argMap.get("-xDim")).intValue();
			yDim = ((Double) argMap.get("-yDim")).intValue();
		} else if (argMap.containsKey("-xDim")) {
			xDim = ((Double) argMap.get("-xDim")).intValue();
			yDim = xDim;
		} else if (argMap.containsKey("-yDim")) {
			yDim = ((Double) argMap.get("-yDim")).intValue();
			xDim = yDim;
		}

		if (argMap.containsKey("-type")) {
			type = ((Double) argMap.get("-type")).intValue();
			if (type < 1 || type > 5) {
				System.err
						.println("\n The only possible predefined types are 1,2,3, 4 and 4");
				System.exit(-1);
			}
		}

		if (argMap.containsKey("-practica")) {
			practica = true;
		}

		if (argMap.containsKey("-batch")) {
			gui = false;
		}

		if (argMap.containsKey("-agent")) {
			agentType = (String) argMap.get("-agent");
		} else {
			System.err
					.println("\n No agent type has been specified, this is a MUST. Example: '-agent search.GreedyAgent");
			System.exit(-1);
		}

		if (argMap.containsKey("-numAngles")) {
			angles = ((Double) argMap.get("-numAngles")).intValue();
		}

		AgentPlayer search = (SearchAgent) getNewSearchAgentByName(agentType,
				angles);

		System.out
				.println("---------------------------------------------------------------------");
		System.out.println(" SETUP summary:");
		if (gui)
			System.out.println(" - GUI mode");
		else
			System.out.println(" - batch mode");
		if (argMap.containsKey("-practica"))
			System.out.println(" ** playing practica ** ");
		else if (argMap.containsKey("-xDim") || argMap.containsKey("-yDim"))
			System.out.println(" - single field with xDim = " + xDim
					+ " and yDim " + yDim);
		else if (argMap.containsKey("-type"))
			System.out
					.println(" - Using predefined field number " + type + ".");
		else
			System.out.println(" - Using DEFAULT predefined field number "
					+ type + ".");
		System.out.println(" - AGENT PLAYER: " + agentType);
		if (argMap.containsKey("-numAngles")) {
			System.out.println(" (angles = " + angles + ")");
		} else {
			System.out.println("--> No numAngles used, default value (= 4).");
		}

		if (!practica)
			System.out.println("- unit = " + unit);
		if ((practica || dimensions) && seed != null)
			System.out.println("- seed = " + seed);
		else if (practica || dimensions)
			System.out.println("- No seed provided");

		System.out
				.println("-------------------------------------------------------------------------");

		System.out.println("\n Starting running SoccerGoal..... \n\n\n");

		if (xDim != -1 && yDim != -1) {
			dimensions = true;
		}

		Field Sfield;
		Game sg = null;
		if (seed != null)
			rd = new Random(seed);

		if (practica) {
			// squence of scenarios randomly set but with fixed dimensions
			int[] dimx = { 4, 8, 16, 32, 32, 40 };
			int[] dimy = { 4, 8, 8, 16, 32, 40 };
			int i, j = 0;

			for (i = 0; i < dimx.length; i++) {
				// Generate field of dimensions dimx[i],dimy[i]
				Sfield = new Field(dimx[i], dimy[i], rd);

				search = (SearchAgent) getNewSearchAgentByName(agentType,
						angles);

				sg = new Game(new String("pr1-it: " + (j + 1)), Sfield, search,
						gui);

				if (fieldInText)
					Sfield.drawFieldInText();

				System.out.println("\n --------> Practica, game " + (j + 1)
						+ " [" + dimx[i] + "X" + dimy[i]
						+ "] -- space unit = 1.0 ------->\n");

				// Play game
				try {
					sg.play(gui);
					System.out
							.println("\npath found by " + agentType + " is :");
					((SearchAgent) sg.getAgent()).printPathFound();
				} catch (NoPathFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
					Main.printStats(sg, search);
					solved[j] = false;
				}

				// for stats
				genNodes[j] = ((SearchAgent) search).getNumGeneratedNodes();
				explNodes[j] = ((SearchAgent) search).getNumExploredNodes();
				expandNodes[j] = ((SearchAgent) search).getNumExpandedNodes();
				simCalls[j] = sg.getSimulationCalls();
				time[j] = sg.totalThinkTime;
				iter[j] = sg.currentIter;
				holes[j] = sg.holes;
				cost[j] = (iter[j]+holes[j]*sg.holeCost);
				

				// pause between games
				try {
					Thread.sleep(Main.pauseBetweenGames);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// refresh for next game
				if (sg.window != null) {
					sg.window.dispose();
					sg.window = null;
				}
				j++;

			}

			for (i = 2; i < dimx.length; i++) {
				// Generate field of dimensions dimx[i],dimy[i]
				Sfield = new Field(dimx[i], dimy[i], rd);

				search = (SearchAgent) getNewSearchAgentByName(agentType,
						angles);
				// set unit to 0.5
				((SearchAgent) search).setUnit(0.5);

				sg = new Game(new String("pr1-it: " + (j + 1)), Sfield, search,
						gui);

				System.out.println("\n --------> Practica, game " + (j + 1)
						+ " [" + dimx[i] + "X" + dimy[i]
						+ "] -- space unit = 0.5 ------->\n");

				if (fieldInText)
					Sfield.drawFieldInText();

				// Play game
				try {
					sg.play(gui);
					System.out
							.println("\npath found by " + agentType + " is :");
					((SearchAgent) sg.getAgent()).printPathFound();
				} catch (NoPathFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
					solved[j] = false;
					Main.printStats(sg, search);

				}

				// for practica stats
				genNodes[j] = ((SearchAgent) search).getNumGeneratedNodes();
				explNodes[j] = ((SearchAgent) search).getNumExploredNodes();
				expandNodes[j] = ((SearchAgent) search).getNumExpandedNodes();
				simCalls[j] = sg.getSimulationCalls();
				time[j] = sg.totalThinkTime;
				iter[j] = sg.currentIter;
				holes[j] = sg.holes;
				cost[j] = (iter[j]+holes[j]*sg.holeCost) * 0.5;
				
				// pause between games
				try {
					Thread.sleep(Main.pauseBetweenGames);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// refresh for next game
				if (sg.window != null) {
					sg.window.dispose();
					sg.window = null;
				}
				j++;
			}

			for (i = 2; i < dimx.length; i++) {
				// Generate field of dimensions dimx[i],dimy[i]
				Sfield = new Field(dimx[i], dimy[i], rd);

				search = (SearchAgent) getNewSearchAgentByName(agentType,
						angles);
				// set unit to 0.25
				((SearchAgent) search).setUnit(0.25);

				sg = new Game(new String("pr1-it: " + (j + 1)), Sfield, search,
						gui);

				System.out.println("\n --------> Practica, game " + (j + 1)
						+ " [" + dimx[i] + "X" + dimy[i]
						+ "] -- space unit = 0.25 ------->\n");

				if (fieldInText)
					Sfield.drawFieldInText();

				// Play game
				try {
					sg.play(gui);
					System.out
							.println("\npath found by " + agentType + " is :");
					((SearchAgent) sg.getAgent()).printPathFound();
				} catch (NoPathFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
					solved[j] = false;
					Main.printStats(sg, search);
				}

				// for practica stats
				genNodes[j] = ((SearchAgent) search).getNumGeneratedNodes();
				explNodes[j] = ((SearchAgent) search).getNumExploredNodes();
				expandNodes[j] = ((SearchAgent) search).getNumExpandedNodes();
				simCalls[j] = sg.getSimulationCalls();
				time[j] = sg.totalThinkTime;
				iter[j] = sg.currentIter;
				holes[j] = sg.holes;
				cost[j] = (iter[j]+holes[j]*sg.holeCost) * 0.25;
				
				// pause between games
				try {
					Thread.sleep(Main.pauseBetweenGames);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// refresh for next game
				if (sg.window != null) {
					sg.window.dispose();
					sg.window = null;
				}
				j++;
			}

			// Last game: 15, agent steps of 0.1
			int last = dimx.length - 1;

			Sfield = new Field(dimx[last], dimy[last], rd);

			search = (SearchAgent) getNewSearchAgentByName(agentType, angles);
			((SearchAgent) search).setUnit(0.1);

			sg = new Game(new String("pr1-it: " + (j + 1)), Sfield, search, gui);

			System.out.println("\n --------> Practica, game " + (j + 1) + " ["
					+ dimx[last] + "X" + dimy[last]
					+ "] -- space unit = 0.1 ------->\n");

			if (fieldInText)
				Sfield.drawFieldInText();
			// set unit to 0.1

			j = 14;

			// Play game
			try {
				sg.play(gui);
				System.out.println("\npath found by " + agentType + " is :");
				((SearchAgent) sg.getAgent()).printPathFound();
			} catch (NoPathFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println(e1.getMessage());
				solved[j] = false;
				Main.printStats(sg, search);
			}

			// for practica stats
			genNodes[j] = ((SearchAgent) search).getNumGeneratedNodes();
			explNodes[j] = ((SearchAgent) search).getNumExploredNodes();
			expandNodes[j] = ((SearchAgent) search).getNumExpandedNodes();
			simCalls[j] = sg.getSimulationCalls();
			time[j] = sg.totalThinkTime;
			iter[j] = sg.currentIter;
			holes[j] = sg.holes;
			cost[j] = (iter[j]+holes[j]*sg.holeCost) * 0.1;

			// pause between games
			try {
				Thread.sleep(Main.pauseBetweenGames);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// one field, of predefined type or with the dimensions

			if (dimensions) { // Generate field of dimensions xDim,yDim
				if (seed != null)
					rd = new Random(seed);
				Sfield = new Field(xDim, yDim, rd);

				search = (SearchAgent) getNewSearchAgentByName(agentType,
						angles);
				((SearchAgent) search).setUnit(unit);

				sg = new Game(new String("x=" + xDim + ",y=" + yDim), Sfield,
						search, gui);
							
				if (fieldInText)
					Sfield.drawFieldInText();
				try {
					sg.play(gui);
					System.out
							.println("\npath found by " + agentType + " is :");
					((SearchAgent) sg.getAgent()).printPathFound();
				} catch (NoPathFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
					Main.printStats(sg, search);
				}

			} else {
				// Generate field of type 'type'
				Sfield = new Field(type);

				search = (SearchAgent) getNewSearchAgentByName(agentType,
						angles);
				((SearchAgent) search).setUnit(unit);

				sg = new Game(new String("type=" + type), Sfield, search, gui);
				try {
					Thread.sleep(Main.pauseBetweenGames * 2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (fieldInText)
					Sfield.drawFieldInText();
				try {
					sg.play(gui);
					System.out
							.println("\npath found by " + agentType + " is :");
					((SearchAgent) sg.getAgent()).printPathFound();
				} catch (NoPathFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
					Main.printStats(sg, search);
				}
				try {
					Thread.sleep(Main.pauseBetweenGames * 2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// pause before ending final screen/game
		try {
			Thread.sleep(Main.pauseBetweenGames * 2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (practica) {

			int numSolved = 0;
			String notSolved = new String("");
			for (int k = 0; k < solved.length; k++) {
				if (solved[k]) {
					numSolved++;

				} else {
					vnotSolved[k] = true;
					if (notSolved.equals(""))
						notSolved = notSolved + "game " + (k + 1);
					else
						notSolved = notSolved + ", game " + (k + 1);
				}
			}

		        System.out.println("\n\n Debugging (iterations & cost per game)");
			for (int k = 0; k < solved.length; k++){
				System.out.println("iter[" + k + "] is " + iter[k]+" ; cost[" + k + "] is " + cost[k] + " (-->" + "holes= " + (int)holes[k] +")");
			}
		        System.out.println("End Debugging\n\n");

			System.out
					.println("\n\n\n============================================================");
			System.out.println("OUTPUT SUMMARY");
			System.out.println(" Solved games: " + numSolved + " out of " + 15
					+ ".");
			System.out.println(" games NOT solved : ( " + notSolved + ")");
			System.out.println("\n EXPANDED NODES (in solved) ===> mean is "
					+ String.format("%.3f", Main.mean(expandNodes, solved))
					+ " nodes.");
			System.out.println("                            ===> total is "
					+ String.format("%.0f", Main.sum(expandNodes, solved))
					+ " nodes.");
			System.out.println("\n EXPLORED NODES (in solved) ===> mean is "
					+ String.format("%.3f", Main.mean(explNodes, solved))
					+ " nodes.");
			System.out.println("                            ===> total is "
					+ String.format("%.0f", Main.sum(explNodes, solved))
					+ " nodes.");
			System.out.println("\n GENERATED NODES (in solved) ===> mean is "
					+ String.format("%.3f", Main.mean(genNodes, solved))
					+ " nodes.");
			System.out.println("                            ===> total is "
					+ String.format("%.0f", Main.sum(genNodes, solved))
					+ " nodes.");

			System.out
					.println("-------------------------------------------------------------");
			System.out.println("\n Thinking time ===> total is     "
					+ String.format("%.5f", Main.sum(time)) + " sec.");
			System.out.println("               ===> when solving "
					+ String.format("%.5f", Main.sum(time, solved)) + " sec.");
			
			System.out.println("\n Iterations ===> total is "
					+ String.format("%.0f", Main.sum(iter)));
			System.out.println("            ===>  when solving  "
					+ String.format("%.0f", Main.sum(iter, solved)));

			System.out.println("\n Simulation calls ===> total is "
					+ String.format("%.0f", Main.sum(simCalls)));

			System.out.println("                  ===>  when solving  "
					+ String.format("%.0f", Main.sum(simCalls, solved)));

			System.out.println("\n Cost ===> total is "
					+ String.format("%.2f", Main.sum(cost)));
			
			System.out.println("      ===>  when solving  "
					+ String.format("%.2f", Main.sum(cost, solved)));
			
			System.out.println("\n [Holes ===> total is "
					+ String.format("%.2f", Main.sum(holes)));
			
			System.out.println("        ===>  when solving  "
					+ String.format("%.2f", Main.sum(holes, solved)) + "]");

			System.out
					.println("-------------------------------------------------------------");
			System.out.println("Note (NOT SOLVED)");
			if (numSolved < 15) {
				System.out
						.println("\n EXPANDED NODES (in NOT solved) ===> mean is "
								+ String.format("%.0f",
										Main.mean(expandNodes, vnotSolved))
								+ " nodes.");
				System.out.println("                            ===> total is "
						+ String.format("%.0f",
								Main.sum(expandNodes, vnotSolved)) + " nodes.");
				System.out
						.println("\n EXPLORED NODES (in NOT solved) ===> mean is "
								+ String.format("%.0f",
										Main.mean(explNodes, vnotSolved))
								+ " nodes.");
				System.out
						.println("                            ===> total is "
								+ String.format("%.0f",
										Main.sum(explNodes, vnotSolved))
								+ " nodes.");
				System.out
						.println("\n GENERATED NODES (in NOT solved) ===> mean is "
								+ String.format("%.0f",
										Main.mean(genNodes, vnotSolved))
								+ " nodes.");
				System.out.println("                            ===> total is "
						+ String.format("%.0f", Main.sum(genNodes, vnotSolved))
						+ " nodes.");

				System.out
						.println("\n Simulation calls (in NOT solved) ===> total is "
								+ String.format("%.0f",
										Main.sum(simCalls, vnotSolved)));
				System.out
						.println("\n Thinking time (in NOT solved) ===> total is "
								+ String.format("%.5f",
										Main.sum(time, vnotSolved)) + " sec.");
			}

			System.out
					.println("============================================================");
		}

		if (sg != null)
			sg.pullThePlug();
		System.out.println("\n\n\n ... SoccerGoal program successfully ended.");
		System.out
				.println("-------------------------------------------------------------------------");

	}
}
