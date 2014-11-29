/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search.desarrollo;


import game.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class BusquedaUniforme extends MySearchAgent {
    boolean debug = false;
	//List of visited positions (states)
	HashSet<DoublePosition> visited = new HashSet<DoublePosition>();
	int seed = 9;
	Random rd = new Random(seed);
        int primero =0;
	//Initially no positions have been tested
	double generatedNum = 0;
	double expandedNum = 0;
	double exploredNum = 0;
         int sigMov[] = new int[10000];
	int step = -1;
        int k=0;
        ArrayList<DoublePosition> recorrido = new ArrayList<DoublePosition>();
        Nodo nod[] = new Nodo[20000000];
               boolean solucion = false;
    public BusquedaUniforme(int numero)
    {
        super(numero);
    }
/*

    public void calcularCamino(Game game) {
          int num = possibleAngles.length;
		game.setHoleCost(10);
                int generados=0;
                int inicial = 0,contador=0;
                int nodoCamino=0,nodoSolucion=0; //nodo escogido por menor coste
		Movement iMove = new Movement();
		DoublePosition iPosition,Position,loc;
                Position = null;
                double minimo =0;
                ArrayList<DoublePosition> visitados = new ArrayList<>();
                ArrayList<Nodo> noVisitados = new ArrayList<>();
                ArrayList<Nodo> repetidos = new ArrayList<>();
                Iterator<Nodo> iterador = noVisitados.listIterator();
              
                Nodo nodpadre = new Nodo();
               int nodoencontrado=0;
	       DoublePosition stateIPosition;

		step++;
         

                       
                        Position = game.field.agentPosition;
                        nod[0] = new Nodo(0);
                        nod[0].agregarPadre(nod[0]);
                        nod[0].agregarPosicion(Position);
                        noVisitados.add(nod[0]);
                        stateIPosition=SearchAgent
				.getNDiscrStateFor(Position);
                          visitados.add(stateIPosition);
                while(!solucion)
                {
                           if (step > 400000){   //Si llega a mas de 40000 interacciones se para el juego
                         solucion=true;
                     }

                     if(game.isInGoal(Position))
                        {
                            exploredNum++;
                            nodoSolucion=nodoCamino;
                             
                            solucion = true;   
                        }
                     
                    else { 
                       
                          nod[nodoCamino].cambiarEstado(true);
                          nodpadre=nod[nodoCamino];
                         
                          expandedNum++;
                          exploredNum++;
                    for(int i=0;i<num;i++)
                    {
                        	iMove = new Movement(Position, unit,
				possibleAngles[i]);
			        iPosition = game.simulateMovement(Position, iMove);
                        
                        
                       if(iPosition!=null ){
                  
                          generatedNum++;
                         step++;
                            generados++;
                            nod[generados] = new Nodo(generados);

                            nod[generados].agregarPosicion(iPosition);
                            nod[generados].agregarPadre(nodpadre);
                                                       
                             nod[generados].añadirCoste(calcularFSegunEstrategia(nod[generados],game));
                          

                             nod[generados].agregarMov(i);
                            noVisitados.add(nod[generados]);
                            
                       
                        }  
                    }

                       Collections.sort(noVisitados, new Comparator<Nodo>() {
                           @Override
                              public int compare(Nodo t,Nodo t1) {
                                    return new Double(t.Coste()).compareTo(t1.Coste());    //Ordena el arraylist de menor a mayor, en caso de empate se pone primero el mas antiguo
                              }

                             
      
                          });
                  
                    iterador = noVisitados.listIterator();
                    
                    
                    
                                    contador=0;
                    nodoCamino=inicial;
                  

       
                  while(nodoencontrado==0){
               try{
                      Nodo comprobar = iterador.next();
              
                      stateIPosition=SearchAgent
				.getNDiscrStateFor(comprobar.posicion());
                            if (!visitados.contains(stateIPosition)){          
                                 visitados.add(stateIPosition);
                                nodoCamino=comprobar.nNodo();
                                nodoencontrado=1;
                                repetidos.add(comprobar);

                            }
                    
                           
                             else{
                                exploredNum++;
                               
                                repetidos.add(comprobar);
                            }
                     }catch(NoSuchElementException e){step=500000; solucion=true; nodoencontrado=1;}   //En caso de error(no hay camino posible) se acaba el juego
                  }
         
                  nodoencontrado=0;
                noVisitados.removeAll(repetidos);
                  repetidos.clear();                       //Los nodos ya descartados por ya ser visitadas sus posiciones se eliman
  
                    Position = nod[nodoCamino].posicion();  //el proximo nodo a expandir
                  
                  
                     }
                    
                }
                                       
              obtenerSolucion(nodoSolucion);
                
                
                
                
    }
   
  public void obtenerSolucion(int nodoSolucion){
      Nodo nodpadre=nod[nodoSolucion];
                while(!nodpadre.equals(nod[0]) && step<=400000){
             
                sigMov[k]= nodpadre.Mov();                           //Tras tener la solucion recorre el camino inverso de la solucion al principio y recoge los movimientos
                
                nodpadre=nodpadre.padre();
                
                 k++;       
                }
  }
     @Override
    public double calcularFSegunEstrategia(Nodo nodo,Game game) {
     if(!game.isInHole(nodo.posicion()))
          return nodo.padre().Coste()+1;
                            
         return nodo.padre().Coste()+game.holeCost;
    }

    @Override
    public int nextMove(Game game) {
        
        if (primero == 0)
        {
           calcularCamino(game);
           
           setNumExpandedNodes(expandedNum);
           setNumExploredNodes(exploredNum);
           setNumGeneratedNodes(generatedNum);
        }
        if (step > 40000) return -1;
        primero++;
        k--;

        return sigMov[k];
        
    }
**/

    @Override
    public void calcularFSegunEstrategia(Game game, int num, DoublePosition iPosition, Nodo aux) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
    
}
