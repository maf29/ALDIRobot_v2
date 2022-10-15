/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package epsevg.upc.edu.c2223;

/**
 *
 * @author Alberto
 */
public class node implements Comparable<node>{
    
    public double distancia;
    public String name;
    //node next;

    public node(double distancia, String name)
    {
        this.distancia = distancia;
        this.name = name;
    }

    @Override
    public int compareTo(node o) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        if(this.distancia < o.distancia) return -1;
        else if(this.distancia > o.distancia) return 1;
        else return 0;  //si son iguales
        
        //return this.distancia.compare(o.distancia);
    }
}
