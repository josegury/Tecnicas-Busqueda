package search.desarrollo;

import game.*;

public class BusquedaAnchura extends MySearchAgent {
    
    public BusquedaAnchura(int num) {
            super(num);
    }

    @Override
    public void calcularFSegunEstrategia(Game game, int num, DoublePosition iPosition, Nodo aux) {
        for (int i = 0; i < num; i++){
            iPosition = game.simulateMovement(aux.posicion,possibleAngles[i]);
            if(iPosition!=null){
                frontera.add(new Nodo(aux,iPosition,i));
                generatedNum++;
            }

        }
    }
    
}
