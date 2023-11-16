package com.desarrollojlcp.structuralengineer2d;

import java.io.Serializable;

public class Carga extends Object implements Serializable {
    protected  double F,L = 0;
    private  int sentido = 0;
    protected  double Rb2n, Rb2f, Rb3n, Rb3f = 0; // que es esto? porque no hay Rb1n??
    protected  double rbnx, rbny, rbfx, rbfy, rbnm, rbfm = 0;
    protected  double a,b = 0;
    protected int casoDeCarga,casoDeApoyo = 0;
    protected double anguloInclinacion = 0;
    protected boolean sistemaInternacional = true;

    public Carga (){
    }

    protected Carga(int casoDeApoyo1, int casoDeCarga1, double longitud, double a1, double magni, double anguloInclinacion1, boolean sisInter) {
          
        sistemaInternacional = sisInter;
        if (sistemaInternacional){
            a = a1;
            b = longitud - a;
            L = longitud;
        } else {
            //se pasa la informacion a plg
            a = (a1);
            L = longitud;
            b = L - a;
        }

        F = magni;
        casoDeCarga = casoDeCarga1;
        casoDeApoyo = casoDeApoyo1;
        anguloInclinacion = anguloInclinacion1;
        setFEM();
    }



    protected void setFEM(){

        if (casoDeApoyo == 1){                                  //apoyo n y f son simplemente apoyados
            if (casoDeCarga == 1){                              //Carga puntual aplicada a una distancia a
                Rb2n = ((F * b) / L);
                Rb2f = ((F * a) / L);
                Rb3n = 0;
                Rb3f = 0;
            } else if (casoDeCarga == 2){                       //carca distribuida rectangular aplicada una distancia a
                Rb2n = ((F * a * (L + b)) / (2 * L));
                Rb2f = ((F * Math.pow(a,2)) / (2 * L));
                Rb3n = 0;
                Rb3f = 0;
            } else if (casoDeCarga == 3){                       //carga distribuida triangular aplicada desde una distancia b desde una distancia a
                Rb2n = ((F * Math.pow(b,2)) / (6 * L));
                Rb2f = (((F * b) / (6 * L)) * ((3 * L) - b));
                Rb3n = 0;
                Rb3f = 0;
            } else if (casoDeCarga == 4){                       //momento F aplicado a una distancia a
                Rb2n = (F / L);
                Rb2f = -1 * (F / L);
                Rb3n = 0;
                Rb3f = 0;
            }

        } else if (casoDeApoyo == 2){                                                       //nodo n empotrado, nodo f simplemente apoyado
            if (casoDeCarga == 1){
                Rb2n = (((F * b) / (2 * L)) * (3 - (Math.pow(b,2) / Math.pow(L,2))));
                Rb2f = ((F * Math.pow(a,2)) / (2 * Math.pow(L,2))) * (3 - (a / L));
                Rb3n = (((F * a * b) * (L + b)) / (2 * Math.pow(L,2)));
                Rb3f = 0;
            } else if (casoDeCarga == 2){
                Rb2n = ((F * a) / 8) * (5 - (Math.pow(b,2) / Math.pow(L,2))) * (1 + (b / L));
                Rb2f = ((F * Math.pow(a,3))/(8 * Math.pow(L,2)))*(4 - (a/L));
                Rb3n = ((F * Math.pow(a,2) * Math.pow((L + b),2))/(8 * Math.pow(L,2)));
                Rb3f = 0;
            } else if (casoDeCarga == 3){
                Rb2n = ((F * Math.pow(b,2))/(40 / L)) * (10 - (Math.pow(b,2)/Math.pow(L,2)));
                Rb2f = ((F * b) / 40) * (20 - ((b / L)* (10 - (Math.pow(b,2)/Math.pow(L,2)))));
                Rb3n = (((F * Math.pow(b,2)) / 120) * (10 - ((3) * (Math.pow(b,2)/Math.pow(L,2)))));
                Rb3f = 0;
            }else if (casoDeCarga == 4){
                Rb2n = (3 / 2) * ((F * a)/Math.pow(L,2)) * (2 - (a/L));
                Rb2f = (-3 / 2) * ((F * a)/Math.pow(L,2)) * (2 - (a / L));
                Rb3n = ((F / 2) * (1 - (3 * (Math.pow(b,2)/Math.pow(L,2)))));
                Rb3f = 0;
            }

        } else if (casoDeApoyo == -2){                                                      //nodo n simplemente apoyado, nodo f empotrado
            //parte experimental
            double c = a;
            a = b;
            b = c;
            if (casoDeCarga == 1){
                Rb2n = ((F * Math.pow(a,2)) / (2 * Math.pow(L,2))) * (3 - (a / L));
                Rb2f = (((F * b) / (2 * L)) * (3 - (Math.pow(b,2) / Math.pow(L,2))));
                Rb3n = 0;
                Rb3f = -1*(((F * a * b) * (L + b)) / (2 * Math.pow(L,2)));
            } else if (casoDeCarga == 2){
                Rb2n = ((F*b)/8)*(8-((b/L)*(6-(Math.pow(b,2)/ Math.pow(L,2)))));
                Rb2f = ((F*Math.pow(b,2))/(8*L))*(6 - (Math.pow(b,2)/Math.pow(L,2)));
                Rb3n = 0;
                Rb3f = -1*((F * Math.pow(a,2) * Math.pow((L + b),2))/(8 * Math.pow(L,2)));
            } else if (casoDeCarga == 3){
                Rb2n = ((F * b) / 40) * (20 - ((b / L) - (Math.pow(b,2)/Math.pow(L,2))));
                Rb2f = ((F*a)/40)*(20-((Math.pow(a,2)/Math.pow(L,2))*(5-(a/L))));
                Rb3n = 0;
                Rb3f = -1*((F*Math.pow(a,2))/120)*(20-(3*(a/L)*(5-(a/L))));
            }
            else if (casoDeCarga == 4){
                Rb2n = (-3 / 2) * ((F * a)/Math.pow(L,2)) * (2 - (a/L));
                Rb2f = (3 / 2) * ((F * a)/Math.pow(L,2)) * (2 - (a/L));
                Rb3n = 0;
                Rb3f = -1*((F / 2) * (1 - (3 * (Math.pow(b,2)/Math.pow(L,2)))));
            }
        } else if (casoDeApoyo == 3){                                                       //nodos n y f empotrados
            if (casoDeCarga == 1){
                Rb2n = F * (Math.pow(b,2)/(Math.pow(L,2))) * (3 - (2 * (b / L)));
                Rb2f = F * (Math.pow(a,2)/(Math.pow(L,2))) * (3 - (2 * (a / L)));
                Rb3n = (F * a * Math.pow(b,2)) / Math.pow(L,2);
                Rb3f = -1* (F * Math.pow(a,2) * b) / Math.pow(L,2);
            } else if (casoDeCarga == 2){
                Rb2n = ((F * a) / 2) * (2 - ((Math.pow(a,2)/Math.pow(L,2)) * (2 - (a / L))));
                Rb2f = ((F * Math.pow(a,3)) / (2 * Math.pow(L,2))) * (2 - (a / L));
                Rb3n = ((F * Math.pow(a,2)) / 12) * (6 - ((a / L) * (8 - (3 * (a / L)))));
                Rb3f = -1*(((F * Math.pow(a,3)) / (12 * L)) * (4 - (3 * (a / L))));
            } else if (casoDeCarga == 3){
                Rb2n = ((F * Math.pow(b,3)) / (20 * Math.pow(L,2))) * (5 - ((2 * b) / L));
                Rb2f = ((F * b) / 20) * (10 - ((Math.pow(b,2) / Math.pow(L,2)) * (5 - (2 * (b / L)))));
                Rb3n = ((F * Math.pow(b,3)) / (60 * L)) * (5 - (3 * (b / L)));
                Rb3f = -1* (((F * Math.pow(b,2)) / 60) * ((3 * (Math.pow(b,2) / Math.pow(L,2))) + (10 * (a / L))));
            } else if (casoDeCarga == 4){
                Rb2n = (F * ((6 * a * b) / Math.pow(L,3)));
                Rb2f = -1 * (F * ((6 * a * b) / Math.pow(L,3)));
                Rb3n = (F * (b / L)) * (2 - (3 * (b / L)));
                Rb3f = ((F * (a / L)) * (2 - (3 * (a / L))));
            }
        }

        rbnx = -1 * (Rb2n * Math.sin(anguloInclinacion));
        rbny = Rb2n * Math.cos(anguloInclinacion);
        rbfx = -1 * (Rb2f * Math.sin(anguloInclinacion));
        rbfy = Rb2f * Math.cos(anguloInclinacion);

        //Para el sistema ingles, se pasan los momentos de Klb-ft a Klb-in para que ingresen a las matrices de analisis
        if (sistemaInternacional){
        rbnm = Rb3n;
        rbfm = Rb3f;
        } else {
            rbnm = Rb3n *12;
            rbfm = Rb3f *12;
        }
    }



    protected double getA(){return  a;}

    protected double getB(){return  b;}


    protected double getF(){return  F;}

    protected int getCasoCarga(){return casoDeCarga;}

}
