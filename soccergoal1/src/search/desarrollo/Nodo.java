/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package search.desarrollo;
import game.*;


public class Nodo {
    Nodo padre;
    DoublePosition posicion;
    int angulo;
    double heuristica;
    double coste;
    
    Nodo(DoublePosition posicion){
        padre=null;
        this.posicion=posicion;
    }
    
    Nodo(DoublePosition posicion,double heuristica,double coste){
        padre=null;
        this.posicion=posicion;
        this.heuristica=heuristica;
        this.coste=coste;
    }
    
    Nodo(Nodo padre,DoublePosition posicion,int angulo){
        this.padre=padre;
        this.angulo=angulo;
        this.posicion=posicion;
    }
    
    Nodo(Nodo padre,DoublePosition posicion,int angulo, double heuristica,double coste){
        this.padre=padre;
        this.angulo=angulo;
        this.posicion=posicion;
        this.heuristica=heuristica;
        this.coste=this.padre.coste+coste;
    }
}
