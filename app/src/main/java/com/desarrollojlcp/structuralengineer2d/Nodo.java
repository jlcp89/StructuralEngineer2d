package com.desarrollojlcp.structuralengineer2d;

import java.io.Serializable;

public class Nodo extends Object implements Serializable {
    int idNodo = 0;
    double coordenadaX;
    double coordenadaY;

    //Identificadores de las reacciones en el nodo
    int r1, r2, r3 = 0;

    //FEM
    double rb1 = 0;
    double rb2 = 0;
    double rb3 = 0;

    //Cargas equivalentes para analisis
    protected  double ra1 = 0;
    protected  double ra2 = 0;
    protected  double ra3 = 0;

    //reacciones de analisis rc
    protected  double rc1 = 0;
    protected  double rc2 = 0;
    protected  double rc3 = 0;

    //reacciones totales finales en el nodo, rt = rb + rc
    protected  double rt1 = 0;
    protected  double rt2 = 0;
    protected  double rt3 = 0;

    //desplazamientos en el nodo
    protected  double des1, des2, des3 = 0;

    //protected  int condicionDeApoyo = 3;
    protected boolean esApoyo = true;
    protected  int apoyo = 0;

    protected Nodo (){}

    protected Nodo(int id,  double coorX, double coorY, int ap, boolean esApoyo1){
        idNodo = id;
        coordenadaX = coorX;
        coordenadaY = coorY;
        apoyo = ap;
        esApoyo = esApoyo1;
    }

    protected void setR1 (int reac1){
        this.r1 = reac1;
    }

    protected void setR2 (int reac2){
        this.r2 = reac2;
    }

    protected void setR3 (int reac3){
        this.r3 = reac3;
    }

    /*public void setCondicionDeApoyo (int condicion){
        this.condicionDeApoyo = condicion;
    }

    public int getCondicionDeApoyo (){
        return condicionDeApoyo;
    }*/

    protected int getId (){
        return idNodo;
    }

    protected void setRt(){
        rt1 = rb1 + rc1;
        rt2 = rb2 + rc2;
        rt3 = rb3 + rc3;
    }

    protected double getCoordenadaX (){
        return coordenadaX;
    }

    protected double getCoordenadaY(){
        return coordenadaY;
    }

    protected void setDes1(double d){
        this.des1 = d;
    }

    protected void setDes2(double d){
        this.des2 = d;
    }

    protected void setDes3(double d){
        this.des3 = d;
    }

    protected double getDes1(){
        return des1;
    }

    protected double getDes2(){
        return des2;
    }

    protected double getDes3(){
        return des3;
    }

    protected int getR1(){
        return r1;
    }

    protected int getR2(){
        return r2;
    }

    protected int getR3(){
        return r3;
    }

    protected void setRc(double reac1, double reac2, double reac3){
        rc1 = reac1;
        rc2 = reac2;
        rc3 = reac3;
    }

    protected void setRtCero(){
        rt1 = 0;
        rt2 = 0;
        rt3 = 0;
    }

    protected double getRt1(){
        return rt1;
    }

    protected double getRt2(){
        return rt2;
    }

    protected double getRt3(){
        return rt3;
    }

    protected int getApoyo(){
        return apoyo;
    }
}
