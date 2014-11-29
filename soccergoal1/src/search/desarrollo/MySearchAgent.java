package search.desarrollo;
import game.*;
import java.util.ArrayList;

public abstract class MySearchAgent extends SearchAgent {
    
    ArrayList<Nodo> frontera = new ArrayList();
    ArrayList<Nodo> camino = new ArrayList();
    ArrayList<DoublePosition> cerrados=new ArrayList();
    boolean solucionado=false;
    double generatedNum = 0;
    double expandedNum = 0;
    double exploredNum = 0;

    public MySearchAgent(int num) {
        super(num);
    }

    @Override
    public int nextMove(Game game) {
        DoublePosition iPosition=game.field.agentPosition;
        Nodo aux;
        int num = possibleAngles.length;
        boolean encerrado=false;
        if(!solucionado){
            aux=new Nodo(iPosition);
            while(!game.isInGoal(aux.posicion)){
                exploredNum++;
                if(!cerrados.contains(aux.posicion)){
                    calcularFSegunEstrategia(game, num, iPosition, aux);
                    expandedNum++;
                    cerrados.add(aux.posicion);
                }
                if(!frontera.isEmpty()){
                aux=frontera.get(0);
                frontera.remove(0);
                }
                else {
                    encerrado=true;
                    break;
                }
            }
            if(!encerrado){
                exploredNum++;
                while(aux.padre!=null){
                camino.add(aux);
                aux=aux.padre;
                }
            }
            setNumExpandedNodes(expandedNum);
            setNumExploredNodes(exploredNum);
            setNumGeneratedNodes(generatedNum);
            if(encerrado) {
                return -1;
            }
            else {
                solucionado=true;
            }
        }
        if(!camino.isEmpty()){
            aux=camino.get(camino.size()-1);
            camino.remove(camino.size()-1);
            return aux.angulo;
        }
        else {
            return -1;
        }
    }
    
    public abstract void calcularFSegunEstrategia(Game game, int num, DoublePosition iPosition, Nodo aux);
}
