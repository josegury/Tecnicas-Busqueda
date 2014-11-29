package game;

/**
 * Exception used when a search agent doesn't find a path, or if the agent doesn't know where to move and returns index -1.
 * @author ssii
 *
 */

public class NoPathFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8414984497718953023L;

	public NoPathFoundException() {

	}

	public NoPathFoundException(String message) {
		super(message);
	}
}
