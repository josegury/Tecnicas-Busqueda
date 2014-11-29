package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;


import javax.imageio.*;
import javax.swing.*;

import java.awt.*;

import javax.swing.JPanel;

import java.util.Random;

/**
 * This class has a double purpose: <br>
 * (1) it defines the field elements (barriers, goal and agent position) and <br>
 * (2) it paints the GUI, if necessary.
 * 
 * @author ssii
 * 
 */
public class Field extends JPanel {

	private static final long serialVersionUID = 6820005809990842415L;

	/**
	 * Dimension for x-axis
	 */
	private int xDim;

	/**
	 * Dimension for y-axis
	 */
	private int yDim;;

	/**
	 * list of barriers or obstacles
	 */
	public ArrayList<BarrierPost> barriersList = new ArrayList<BarrierPost>();


	/**
	 * list of holes
	 */
	public ArrayList<HolePost> holesList = new ArrayList<HolePost>();

	/**
	 * positions that form the goal
	 */
	private ArrayList<Position> goal = new ArrayList<Position>();

	/**
	 * position (in the real space) where the agent is located (at each game
	 * step)
	 */
	public DoublePosition agentPosition;

	/**
	 * if it's true, the game will paint the positions the agent goes into from
	 * step 0 till now
	 */
	boolean estela = true;

	/**
	 * A way to know the tree game, this is the current state and knows what the
	 * history behind
	 */
	GameStep gameState;

	/**
	 * it will maintain a correspondence for positions with an obstacle to the
	 * barrier. The {@link java.util.HashMap} structure makes it faster to
	 * access.
	 */
	HashMap<Position, BarrierPost> elements = new HashMap<Position, BarrierPost>();
	HashMap<Position, HolePost> holes = new HashMap<Position, HolePost>();
    

    //BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB)

	/**
	 * ball image, for GUI visualization
	 */
	Image ball;
	Graphics2D labG;
	BufferedImage labImg;

        /* Color for the holes */
        Color holeColor=new Color(110,200,0);

	// more colors: see http://kb.iu.edu/data/aetf.html

	// it will determined the amount of obstacles: from none (=0.0) to all
	// positions (1.0)
	private double barrierDensity;

	/**
	 * ppu means pixels per unit. It has been set up to 20, and this means that
	 * every square unit will be represented in the screen by this 20 pixels. <br>
	 * This could be changed in future versions, but then image for ball should
	 * also be resized.
	 */


        private double holeDensity;

       /**
        * See previous comment on barrierDensity. The same applies in this case 
	*/

    
	public static int ppu = 20;
	private Dimension d;
	// End of GUI variables

	/**
	 * These three Positions {@link #midGoal}, {@link #leftGoal},
	 * {@link #rightGoal} are related to the goal, just in case an agent wants
	 * to access them midGoal indicates the middle {@link game.Position},
	 * leftGoal the left one and rightGoal the right one. Be careful because
	 * they are <it>integer </it> positions, for example one single Position for
	 * the goal in <5,0> will make all the same, but the top left would be (in
	 * real numbers of DoublePosition) <5,0>, the bottom left <5,1>, the exactly
	 * middle or center <5.5,0.5>, the top right <6,0> and the bottom right
	 * <6,1>. see {@link game.Position} and {@link game.DoublePosition} for
	 * further detail.
	 */
	private Position midGoal;
	private Position leftGoal;
	private Position rightGoal;

	public double getBarrierDensity() {
		return barrierDensity;
	}

	public void setBarrierDensity(double barrierDensity) {
		this.barrierDensity = barrierDensity;
	}


	public double getHoleDensity() {
		return holeDensity;
	}

	public void setHoleDensity(double holeDensity) {
		this.holeDensity = holeDensity;
	}


	/**
	 * 
	 * @return the left {@link Position} of the goal. Be careful, it is an
	 *         integer position. For example, if it is (5,0) it will refer to
	 *         the top left of the left position, in real numbers, for example,
	 *         the bottom left will be (5,1).
	 */
	public Position getLeftGoal() {
		return leftGoal;
	}

	public void setLeftGoal(Position leftGoal) {
		this.leftGoal = leftGoal;
	}

	/**
	 * 
	 * @return the right {@link Position} of the goal. Be careful, it is an
	 *         integer position. For example, if it is (6,0) it will refer to
	 *         the top left of the right position, in real numbers, for example,
	 *         the bottom right of the right position of will be (7,1).
	 */
	public Position getRightGoal() {
		return rightGoal;
	}

	public void setRightGoal(Position rightGoal) {
		this.rightGoal = rightGoal;
	}

	/**
	 * 
	 * @return the middle {@link Position} of the goal. Be careful, it is an
	 *         integer position. For example, if it is (3,0) it will refer to
	 *         the top left of the middle position, in real numbers, for
	 *         example, the exactly middle of it will be (3.5,0.5). If the goal
	 *         has an even number of positions, it will provide length/2 as the
	 *         mid-position, for 6, it will be 3: <br>
	 *         0 1 2 3 4 5 G G G M G G
	 */
	public Position getMidGoal() {
		return midGoal;
	}

	public ArrayList<Position> getGoal() {
		return goal;
	}

	public void setGoal(ArrayList<Position> goal) {
		this.goal = goal;
	}

	/**
	 * 
	 * @param left
	 *            indicates the left {@link game.Position} of the goal
	 * @param goalLength
	 *            indicates how many positions form the goal
	 */

	public void setGoal(Position left, int goalLength) {
		int yGoal = left.getY();
		int xGoal = left.getX();
		leftGoal = new Position(xGoal, yGoal);
		for (int j = left.getX(); j < (xGoal + goalLength); j++) {
			goal.add(new Position(j, yGoal));
		}
		rightGoal = new Position((xGoal + goalLength - 1), yGoal);
		midGoal = new Position(xGoal + (goalLength / 2), yGoal);

	}

	public int getXDim() {
		return xDim;
	}

	public void setxDim(int xDim) {
		this.xDim = xDim;
	}

	public int getYDim() {
		return yDim;
	}

	public void setyDim(int yDim) {
		this.yDim = yDim;
	}

	public DoublePosition getAgentPosition() {
		return agentPosition;
	}

	public void setAgentPosition(DoublePosition agentPosition) {
		this.agentPosition = agentPosition;
	}

	/**
	 * this method introduces in the HashMap the info about the obstacles, in
	 * case it wasn't done <it> at hand </it> previously.
	 */
	private void fillBarrierHashMap() {

		for (BarrierPost barrierElement : this.barriersList) {

			this.elements.put(barrierElement.pos, barrierElement);

		}

	}


	/**
	 * this method introduces in the HashMap the info about the obstacles, in
	 * case it wasn't done <it> at hand </it> previously.
	 */
	private void fillHoleHashMap() {

		for (HolePost holeElement : this.holesList) {

			this.holes.put(holeElement.pos, holeElement);

		}

	}


	/**
	 * this method generates the most simple field: 3x3, the goal is only one
	 * integer position located in (1,0). agent starts at (0,2)
	 * 
	 * @param post
	 *            if it is true, there will be an obstacle in Position (1,1)
	 */
	public void generateMostSimple3by3(boolean post) {

		xDim = 3;
		yDim = 3;

		this.setAgentPosition(new DoublePosition(0, yDim - 1));

		this.setGoal(new Position(1, 0), 1);

		if (post) {
			barriersList.add(new BarrierPost(1, 1));
			elements.put(new Position(1, 1), new BarrierPost(1, 1));
		}

		// myField.fillBarrierHashMap();

	}

	// Fixed scenarios
	/**
	 * For debugging purposes there are four predefined scenarios:
	 * 
	 * @param scenario
	 *            a scheme for 1 can be found at {@link https
	 *            ://www.dropbox.com/s/gq0icruogzzwh94/escenario1.png}, a scheme
	 *            for 2 can be found at {@link https
	 *            ://www.dropbox.com/s/4ucl415bqgvofqb/escenario2.png}. For
	 *            cases 3 and 4, please see method
	 *            {@link #generateMostSimple3by3(boolean)}.
	 */
	Field(int scenario) {

		switch (scenario) {
		case 1:
			// scenario1: see image in
			// https://www.dropbox.com/s/gq0icruogzzwh94/escenario1.png
			xDim = 16;
			yDim = 32;
			barriersList.add(new BarrierPost(3, 24));
			barriersList.add(new BarrierPost(3, 25));
			barriersList.add(new BarrierPost(3, 31));

			for (int i = 20; i < 29; i++) {
				barriersList.add(new BarrierPost(8, i));
			}

			barriersList.add(new BarrierPost(11, 22));
			barriersList.add(new BarrierPost(6, 17));
			barriersList.add(new BarrierPost(11, 17));

			for (int i = 2; i < 5; i++) {
				barriersList.add(new BarrierPost(i, 14));
			}

			barriersList.add(new BarrierPost(11, 12));
			barriersList.add(new BarrierPost(6, 11));
			barriersList.add(new BarrierPost(6, 7));
			barriersList.add(new BarrierPost(6, 8));
			barriersList.add(new BarrierPost(5, 6));
			barriersList.add(new BarrierPost(11, 6));
			barriersList.add(new BarrierPost(4, 5));
			barriersList.add(new BarrierPost(3, 4));


			holesList.add(new HolePost(0,4));
			holesList.add(new HolePost(0,9));
			holesList.add(new HolePost(0,16));
			holesList.add(new HolePost(0,27));

			holesList.add(new HolePost(1,7));
			holesList.add(new HolePost(1,18));

			holesList.add(new HolePost(1,7));
			holesList.add(new HolePost(1,18));
	
			holesList.add(new HolePost(10,4));
			holesList.add(new HolePost(9,5));
			holesList.add(new HolePost(7,2));
			holesList.add(new HolePost(8,2));


			fillHoleHashMap();

			agentPosition = new DoublePosition(0, 31);
			this.setGoal(new Position(5, 0), 6);
			fillBarrierHashMap();
			break;

		case 2:
			// scenario2: see image in
			// https://www.dropbox.com/s/4ucl415bqgvofqb/escenario2.png
			xDim = 24;
			yDim = 32;
			barriersList.add(new BarrierPost(3, 24));
			barriersList.add(new BarrierPost(3, 25));
			barriersList.add(new BarrierPost(3, 31));

			for (int i = 20; i < 29; i++) {
				barriersList.add(new BarrierPost(8, i));
			}

			for (int i = 0; i < 3; i++) {
				barriersList.add(new BarrierPost(i, 22));
			}

			barriersList.add(new BarrierPost(11, 22));
			barriersList.add(new BarrierPost(6, 17));
			barriersList.add(new BarrierPost(11, 17));

			for (int i = 2; i < 5; i++) {
				barriersList.add(new BarrierPost(i, 14));
			}

			barriersList.add(new BarrierPost(11, 12));
			barriersList.add(new BarrierPost(6, 11));

			barriersList.add(new BarrierPost(13, 9));
			barriersList.add(new BarrierPost(14, 9));
			barriersList.add(new BarrierPost(6, 7));
			barriersList.add(new BarrierPost(6, 8));
			barriersList.add(new BarrierPost(11, 6));
			// Diagonal hacia abajo
			barriersList.add(new BarrierPost(20, 17));
			barriersList.add(new BarrierPost(19, 16));
			barriersList.add(new BarrierPost(18, 15));
			barriersList.add(new BarrierPost(17, 14));
			barriersList.add(new BarrierPost(16, 13));

			barriersList.add(new BarrierPost(20, 24));
			barriersList.add(new BarrierPost(19, 25));
			barriersList.add(new BarrierPost(18, 26));
			barriersList.add(new BarrierPost(17, 27));
			barriersList.add(new BarrierPost(16, 28));





			holesList.add(new HolePost(4,28));
			holesList.add(new HolePost(5,27));
			holesList.add(new HolePost(3,20));
			holesList.add(new HolePost(5,12));
			holesList.add(new HolePost(6,14));
			holesList.add(new HolePost(7,9));
			holesList.add(new HolePost(8,8));
			holesList.add(new HolePost(9,7));
			holesList.add(new HolePost(10,7));


			holesList.add(new HolePost(13,4));
			holesList.add(new HolePost(12,3));


			agentPosition = new DoublePosition(0, 31);
			this.setGoal(new Position(10, 0), 4);




			// holesList.add(new HolePost(0,9));
			// holesList.add(new HolePost(0,16));
			// holesList.add(new HolePost(0,27));

			// holesList.add(new HolePost(1,7));
			// holesList.add(new HolePost(1,18));

			// holesList.add(new HolePost(1,7));
			// holesList.add(new HolePost(1,18));
	
			// holesList.add(new HolePost(10,4));
			// holesList.add(new HolePost(9,5));
			// holesList.add(new HolePost(7,2));
			// holesList.add(new HolePost(8,2));


			fillHoleHashMap();

			fillBarrierHashMap();
			break;

		case 3:
			// the most simple field, only 3x3, one unit goal (top, middle) if
			// true post in the middle, if false no post
			this.generateMostSimple3by3(true);
			break;

		case 4:
			// the most simple field, only 3x3, one unit goal (top, middle) if
			// true post in the middle, if false no post
			this.generateMostSimple3by3(false);
			break;
			
		case 5: //example to be done by hand (exercise)
			xDim = 4;
			yDim = 4;
			barriersList.add(new BarrierPost(1, 0));
			barriersList.add(new BarrierPost(1, 2));
			holesList.add(new HolePost(0,2));
			agentPosition = new DoublePosition(0, 3);
			this.setGoal(new Position(0, 0), 1);
			fillHoleHashMap();

			fillBarrierHashMap();
			
			break;
		}

		int width = xDim * ppu;
		int height = yDim * ppu;
		this.d = new Dimension(width, height);
		this.setFocusable(true);
	}

	/**
	 * 
	 * @param x
	 *            dimension for the field for x-axis (it should be >0 and <= 60)
	 * @param y
	 *            dimension for the field for y-axis (it should be >0 and <= 40)
	 * @param rd
	 *            {@link java.util.Random} generator.
	 */
	public Field(int x, int y, Random rd) {

 	        int numberOfPosts, numberOfHoles, randomX, randomY, coin, midX, goalL;
		Position pos;

		xDim = x;
		yDim = y;

		if (xDim <= 0) {
			System.err
					.println("Minimum dimension for x dimension is 3, so the program changed that value");
			xDim = 3;
		}

		if (yDim <= 0) {
			System.err
					.println("Minimum dimension for y dimension is 3, so the program changed that value");
			yDim = 3;
		}

		if (xDim > 60) {
			System.err
					.println("Maximum dimension for x dimension is 60, so the program changed that value");
			xDim = 60;
		}

		if (yDim > 40) {
			System.err
					.println("Maximum dimension for y dimension is 40, so the program changed that value");
			yDim = 40;
		}

		/*
		 * the agent would randomly located in the bottom left position of the
		 * field (coin==0), in the bottom right (coin==1) or in the bottom
		 * center (coin==0)
		 */
		coin = rd.nextInt(3);

		if (coin == 0) {
			agentPosition = new DoublePosition(0, yDim - 1);
		} else if (coin == 1) {
			agentPosition = new DoublePosition(xDim - 1, yDim - 1);

		} else
			agentPosition = new DoublePosition(xDim / 2, yDim - 1);

		// the left of the goal is set up to 1/5 of the x dimension
		midX = xDim / 2;
		goalL = xDim / 5;

		// at least one position for the goal
		if (goalL == 0)
			goalL = 1;

		this.setGoal(new Position(midX - goalL / 2, 0), goalL);

		barrierDensity = 0.35;

		numberOfPosts = rd.nextInt((int) ((xDim * yDim) * this.barrierDensity));

		// it intrdoduces randomly numberOfPosts obstacles
		for (int k = 0; k < numberOfPosts; k++) {

			randomX = rd.nextInt(xDim);
			randomY = rd.nextInt(yDim);
			pos = new Position(randomX, randomY);

			// if these obstacles already are in the field, re-generate random
			// numbers again
			while (elements.containsKey(pos)) {
				randomX = rd.nextInt(xDim);
				randomY = rd.nextInt(yDim);
				pos = new Position(randomX, randomY);
			}

			/*
			 * if this position is not occupied by the goal or the agent,
			 * introduce it in the field and the corresponding HashMap
			 */
			if (!goal.contains(pos)
					&& !pos.equals(agentPosition.toIntFloorPosition())) {
				barriersList.add(new BarrierPost(randomX, randomY));
				elements.put(new Position(randomX, randomY), new BarrierPost(
						randomX, randomY));
			}

		}

		holeDensity = 0.35;

		numberOfHoles = rd.nextInt((int) ((xDim * yDim) * this.holeDensity));

		// it generates the holes
		for (int k = 0; k < numberOfHoles; k++) {

			randomX = rd.nextInt(xDim);
			randomY = rd.nextInt(yDim);
			pos = new Position(randomX, randomY);

			// if these obstacles already are in the field, re-generate random
			// numbers again
			while (elements.containsKey(pos)||holes.containsKey(pos)) {
				randomX = rd.nextInt(xDim);
				randomY = rd.nextInt(yDim);
				pos = new Position(randomX, randomY);
			}


			if (!goal.contains(pos)
					&& !pos.equals(agentPosition.toIntFloorPosition())) {
				holesList.add(new HolePost(randomX, randomY));
				holes.put(new Position(randomX, randomY), new HolePost(
						randomX, randomY));
			}
		}




		// necessary for GUI purposes
		int width = xDim * ppu;
		int height = yDim * ppu;
		this.d = new Dimension(width, height);
		this.setFocusable(true);

	}

	public Field() {
		super();
		int width = xDim * ppu;
		int height = yDim * ppu;
		this.d = new Dimension(width, height);
		this.setFocusable(true);
	}

	/**
	 * this method will show the field in text format
	 */
	public void drawFieldInText() {
		int i, j;
		Position pos;

		// Top border (dimension: x coordinate)
		for (i = 0; i <= xDim; i++)
			System.out.print("-");
		System.out.println(""); // next line

		// Let's print the field, line by line, i-st line represents y
		// coordinate
		for (i = 0; i < yDim; i++) {
			System.out.print("|"); // left border each line

			for (j = 0; j < xDim; j++) {
				pos = new Position(j, i);
				if (j == Math.round(agentPosition.realX)
						&& i == Math.round(agentPosition.realY))
					System.out.print("A"); // j is the x coordinate, i is the y
											// coordinate
				else if (this.elements.get(pos) != null)
					System.out.print("*");
				else if (this.holes.get(pos) != null)
				        System.out.print("O");

				else if (goal.contains(pos)) {
					System.out.print("g");
				} else
					System.out.print(" ");
			}
			System.out.println("|");
		}

		// Bottom border (dimension: x coordinate)
		for (i = 0; i <= xDim; i++)
			System.out.print("-");

		// New line
		System.out.println("");
	}

	/**
	 * Draw the component
	 */
	public void paintComponent(Graphics go) {
		Position pos;

		Graphics2D g = (Graphics2D) go;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// for all square units (ppu pixels)
		for (int i = 0; i < this.xDim; i++)
			for (int j = 0; j < this.yDim; j++) {
				pos = new Position(i, j);
				// if there is an obstacle --> paint in red
				if (this.elements.get(pos) != null) {
					g.setColor(Color.RED);
					g.fillRect(i * ppu, j * ppu, ppu, ppu);
				}

				else if (this.holes.get(pos) != null) {
  				        g.setColor(holeColor);
					g.fillRect(i * ppu, j * ppu, ppu, ppu);
				}


				else if (goal.contains(pos)) { // if the goal is there -->
							// paint in white
					g.setColor(Color.WHITE);
					g.fillRect(i * ppu, j * ppu, ppu, ppu);
				} else { // else (empty) --> paint in green
					g.setColor(Color.GREEN);
					g.fillRect(i * ppu, j * ppu, ppu, ppu);
				}
			}

		// draw the agent in its position with ball image, or if any error,
		// black circle
		try {
			ImageIcon iid = new ImageIcon(this.getClass().getResource(
					"ballPeq20.png"));
			ball = iid.getImage();
			g.drawImage(ball,
					(int) Math.round(this.agentPosition.getRealX() * ppu),
					(int) Math.round(this.agentPosition.getRealY() * ppu), this);
		} catch (Exception e) {
			g.setColor(Color.BLACK);
			g.fillOval((int) Math.round(agentPosition.realX * ppu),
					(int) Math.round(agentPosition.realY * ppu), ppu, ppu);

		}
		// Now paint the path done till now in the game, with (non-filled) gray
		// circles
		GameStep current = null;
		if (gameState != null && (gameState.parent != null))
			current = gameState.parent;
		if (current != null)
			if (estela) {
				g.setColor(Color.gray);
				while (current.parent != null) {
					drawEstela(current.agentPosition, g);
					current = current.parent;
				}
				// initial position is marked different: filled yellow circle
				g.setColor(Color.yellow);
				g.fillOval((int) Math.round(current.agentPosition.realX * ppu),
						(int) Math.round(current.agentPosition.realY * ppu),
						ppu, ppu);

			}
	}

	/**
	 * draw the history of the agent
	 */
	private void drawEstela(DoublePosition p, Graphics g) {
		g.drawOval((int) Math.round(p.realX * ppu),
				(int) Math.round(p.realY * ppu), ppu, ppu);
	}

	/** @return component dimension */
	public Dimension getPreferredSize() {
		return d;
	}

}
