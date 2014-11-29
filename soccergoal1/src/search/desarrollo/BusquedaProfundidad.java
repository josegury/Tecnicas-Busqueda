package search.desarrollo;
import game.*;
import java.util.ArrayList;

public class BusquedaProfundidad extends MySearchAgent {
    ArrayList<Nodo> ordenaNodos =new ArrayList();

    public BusquedaProfundidad(int num) {
            super(num);
    }

    @Override
    public void calcularFSegunEstrategia(Game game, int num, DoublePosition iPosition, Nodo aux) {
        Movement iMove;
        Nodo hijo;
        for (int i = 0; i < num; i++){
            iMove = new Movement(aux.posicion, unit,possibleAngles[i]);
            iPosition = game.simulateMovement(aux.posicion,iMove);
            if(iPosition!=null){
                hijo=new Nodo(aux,iPosition,i);
                ordenaNodos.add(hijo);
                generatedNum++;
            }   
        }
        while(!ordenaNodos.isEmpty()){
            frontera.add(ordenaNodos.get(ordenaNodos.size()-1));
            ordenaNodos.remove(ordenaNodos.size()-1);
        }
    }
      
}
