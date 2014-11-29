package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing a state of the board, as defined by the SoccerGame class.
 * Contains the location of the agent, (as well as the path followed until now)
 *    
 * Equals and hashCode are defined on all fields EXCEPT the parent.
 * 
 * @author grenager
 *
 */
public class GameStep {
  
  DoublePosition agentPosition;
  
  //Field soccerField;
  
  GameStep parent;
  
  
  /*public Field getSoccerField() {
	return soccerField;
  }

  public void setSoccerField(Field soccerField) {
	this.soccerField = soccerField;
  }*/

  public DoublePosition getAgentPosition() {
    return agentPosition;
  }
  
  public GameStep getParent() {
    return parent;
  }
  
  public List<GameStep> getHistory() {
    List<GameStep> history = new ArrayList<GameStep>();
    GameStep s = this;
    while (s!=null) {
      history.add(s);
      s = s.getParent();
    }
    Collections.reverse(history); // now it should end with this
    return history;
  }
  
  /**
   * Equals and hashCode are defined on all fields EXCEPT the parent.
   */
  public boolean equals(Object o) {
    if (! (o instanceof GameStep)) return false;
    GameStep s = (GameStep) o;
    if (s.agentPosition==null ? agentPosition!=null : !s.agentPosition.equals(agentPosition)) return false;
    return true;
  }
  
  public String toString() {
    String result = "agent=" + agentPosition; //JULIa + angle
    return result;
  }
  
  
  
   
  //Importante: el primer estado tiene que hacer un setSoccerField para conectarlo
  public GameStep(DoublePosition myAgentPosition, GameStep parent){//, Field soccerfield) {
    this.agentPosition = myAgentPosition;
    this.parent = parent;
    
    /*if (this.parent == null)
    	this.soccerField = soccerField;
    else
        this.soccerField = parent.soccerField;*/
  }

}