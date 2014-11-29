package search.desarrollo;
import game.*;


public class BusquedaAEstrella extends MySearchAgent {

    public BusquedaAEstrella(int num) {
            super(num);
    }

    @Override
    public void calcularFSegunEstrategia(Game game, int num, DoublePosition iPosition, Nodo aux) {
        Nodo hijo;
        double heuristica;
        for (int i = 0; i < num; i++){
            iPosition = game.simulateMovement(aux.posicion,possibleAngles[i]);
            if(iPosition!=null){
                heuristica=DoublePosition.euclideanDistance(iPosition,game.field.getMidGoal().toDoublePosition());
                hijo=new Nodo(aux,iPosition,i,heuristica,this.unit);
                frontera.add(hijo);
                generatedNum++;
            }
        }
    }
      
}