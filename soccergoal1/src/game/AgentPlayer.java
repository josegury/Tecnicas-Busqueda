package game;

/**
 * An interface for classes that can choose agents's moves 
 * @author ssii
 *
 */


public interface AgentPlayer {
  
  /**
   * Chooses a Movement for the agent player in the Game.
   * @param game
   * @return a Movement
 * @throws NoPathFoundException 
   */
  Movement move(Game game) throws NoPathFoundException;  
  
}
