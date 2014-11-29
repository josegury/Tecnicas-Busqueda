package search.desarrollo;
import game.*;
import java.util.Collections;
import java.util.Comparator;

public class BusquedaCosteUniforme extends MySearchAgent {
    
    public BusquedaCosteUniforme (int num) {
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
        Collections.sort(frontera, new Comparator<Nodo>() {

            @Override
            public int compare(Nodo o1, Nodo o2) {
                if(o1.coste<=o2.coste){
                    return 1;
                }
                return 0;
            }
        });
    }
}
