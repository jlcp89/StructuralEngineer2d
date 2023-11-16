package com.desarrollojlcp.structuralengineer2d;

import java.io.Serializable;

public class SeccionTransversal extends Object implements Serializable {

    protected int id = 0;
    //area de la seccion transversal, en cm2
    protected  double areaMilimetros = 0;
    protected  double areaMetros = 0;

    //inercia de la seccion transversal, en cm4
    protected  double inerciaMilimetros = 0;
    protected  double inerciaMetros = 0;

    //modulo de elasticidad del material, en GPa
    protected  double moduloElasticidadGPA = 0;
    protected  double moduloElasticidad_NsobreM2 = 0;

    //Producto para opereracion de matriz de rigidez del elemento
    protected  double AE = 0;
    protected  double EI = 0;

    protected double areaIn, inerciaIn, moduloKsi, moduloPsi;

    protected double area, inercia, modulo;

    protected int sistemaUnidades = 0;


    protected SeccionTransversal() {

    }

    protected void setPropiedadesSistemaInternacional(int idS, double area1, double inercia1, double modulo1){
        id = idS;
        areaMilimetros = area1;
        inerciaMilimetros = inercia1;
        moduloElasticidadGPA = modulo1;

        //se guarda el area en centimetros2 y en metros2
        areaMetros = areaMilimetros * 0.000001;

        //se guarda la inercia encentimetros4 y metros4
        inerciaMetros = inerciaMilimetros * 0.000000000001;

        //se guarda el modulo de elasticidad en GPa y Pa (N/m2)
        moduloElasticidad_NsobreM2 = moduloElasticidadGPA * Math.pow(10,9);

        //se guardan los productos AE y EI para su facil uso al ensamblar la matriz de rigidez del
        //elemento
        AE = areaMetros * moduloElasticidad_NsobreM2;
        EI = moduloElasticidad_NsobreM2 * inerciaMetros;
        area = areaMetros;
        inercia = inerciaMetros;
        modulo = moduloElasticidad_NsobreM2;
    }

    protected void setPropiedadesSistemaIngles(int idS, double area1, double inercia1, double modulo1){
        id = idS;
        areaIn = area1;
        inerciaIn = inercia1;
        moduloKsi = modulo1;

        //se guarda el modulo de elasticidad de Ksi a Psi
        moduloPsi = moduloKsi * Math.pow(10,3);

        //se guardan los productos AE y EI para su facil uso al ensamblar la matriz de rigidez del
        //elemento
        AE = areaIn * moduloPsi;
        EI = moduloPsi * inerciaIn;

        area = areaIn;
        inercia = inerciaIn;
        modulo = moduloPsi;
    }


}
