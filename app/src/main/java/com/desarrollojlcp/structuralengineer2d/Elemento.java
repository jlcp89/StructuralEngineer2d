package com.desarrollojlcp.structuralengineer2d;

import java.io.Serializable;
import java.util.Vector;

public class Elemento extends Object implements Serializable {

    //Se crea la seccion transversal del elemento
    protected SeccionTransversal seccionElemento = new SeccionTransversal();

    //Longitud del elemenento en metros
    protected  double L = 0;

    int id = 0;
    protected Nodo nodoN = new Nodo();
    protected Nodo nodoF = new Nodo();
    private  double AEsobreL = 0;
    private  double DoceEIsobreL3 = 0;
    private  double SeisEIsobreL2 = 0;
    private  double CuatroEIsobreL = 0;
    private  double DosEIsobreL = 0;
    private  double deltaX = 0;
    private  double deltaY = 0;
    protected  double lambdaX = 0;
    protected  double lambdaY = 0;
    protected  double matrizKLocal [][] = new double[6][6];
    protected  double matrizKGlobal [][];
    protected  double matrizTDesplazamientos [][] = new double[6][6];
    protected  double matrizTFuerzas [][] = new double[6][6];
    protected  Vector<Carga> cargas = new Vector<Carga>();
    protected  double anguloInclinacion = 0;
    protected  int condicionDeApoyo;
    protected  Carga cargaFEMResultante = new Carga();
    protected  Carga cargaAnalisis = new Carga();
    protected  double desplazamientos [][] = new double[6][1];
    protected  double qInternas [][] = new double[6][1];
    protected  double qInternasF [][] = new double[6][1];
    protected  double qFEM [][] = new double[6][1];
    protected  double qNodos [][] = new double[6][1];
    protected  double rbnx, rbny, rbfx, rbfy, rbnm, rbfm = 0;
    protected boolean sistemaInternacinal = true;



    protected Elemento (){
    }

    protected Elemento (int idE, SeccionTransversal secTrans, Nodo nN, Nodo nF, boolean sisInternacional){
        sistemaInternacinal = sisInternacional;
        id = idE;
        setSeccionElemento(secTrans);
        nodoN = nN;
        nodoF = nF;
        setCondicionDeApoyodelElemento();
        deltaX = nodoF.coordenadaX - nodoN.coordenadaX;
        deltaY = nodoF.coordenadaY - nodoN.coordenadaY;
        if (getDeltaX() != 0){
            anguloInclinacion = Math.atan(getDeltaY()/getDeltaX());
        } else {
            if (getDeltaY()>0){
                anguloInclinacion = Math.toRadians(90);
            } else if (getDeltaY()<0){
                anguloInclinacion = Math.toRadians(-90);
            }
        }

        double L_in;
        if (sistemaInternacinal){
            L = (Math.sqrt((Math.pow(deltaX,2)) + (Math.pow(deltaY,2))));
            lambdaX = (deltaX) / L;
            lambdaY = (deltaY) / L;
            L_in = L;
        }else {
            L = (Math.sqrt((Math.pow(deltaX,2)) + (Math.pow(deltaY,2))));
            lambdaX = (deltaX) / L;
            lambdaY = (deltaY) / L;
            L_in = L * 12;
        }

        AEsobreL = seccionElemento.AE / L_in;
        DoceEIsobreL3 = (12 * seccionElemento.EI) / Math.pow(L_in,3);
        SeisEIsobreL2 = (6 * seccionElemento.EI) / Math.pow(L_in,2);
        CuatroEIsobreL = (4 * seccionElemento.EI) / L_in;
        DosEIsobreL = (2 * seccionElemento.EI) / L_in;

        setMatrizKGlobal();


    }

    private void setMatrizKLocal(){
        this.matrizKLocal[0][0] = AEsobreL;
        this.matrizKLocal[0][1] = 0;
        this.matrizKLocal[0][2] = 0;
        this.matrizKLocal[0][3] = -1 * AEsobreL;
        this.matrizKLocal[0][4] = 0;
        this.matrizKLocal[0][5] = 0;
        this.matrizKLocal[1][0] = 0;
        this.matrizKLocal[1][1] = DoceEIsobreL3;
        this.matrizKLocal[1][2] = SeisEIsobreL2;
        this.matrizKLocal[1][3] = 0;
        this.matrizKLocal[1][4] = -1 * DoceEIsobreL3;
        this.matrizKLocal[1][5] = SeisEIsobreL2;
        this.matrizKLocal[2][0] = 0;
        this.matrizKLocal[2][1] = SeisEIsobreL2;
        this.matrizKLocal[2][2] = CuatroEIsobreL;
        this.matrizKLocal[2][3] = 0;
        this.matrizKLocal[2][4] = -1 * SeisEIsobreL2;
        this.matrizKLocal[2][5] = DosEIsobreL;
        this.matrizKLocal[3][0] = -1 * AEsobreL;
        this.matrizKLocal[3][1] = 0;
        this.matrizKLocal[3][2] = 0;
        this.matrizKLocal[3][3] = AEsobreL;
        this.matrizKLocal[3][4] = 0;
        this.matrizKLocal[3][5] = 0;
        this.matrizKLocal[4][0] = 0;
        this.matrizKLocal[4][1] = -1 * DoceEIsobreL3;
        this.matrizKLocal[4][2] = -1 * SeisEIsobreL2;
        this.matrizKLocal[4][3] = 0;
        this.matrizKLocal[4][4] = DoceEIsobreL3;
        this.matrizKLocal[4][5] = -1 * SeisEIsobreL2;
        this.matrizKLocal[5][0] = 0;
        this.matrizKLocal[5][1] = SeisEIsobreL2;
        this.matrizKLocal[5][2] = DosEIsobreL;
        this.matrizKLocal[5][3] = 0;
        this.matrizKLocal[5][4] = -1 * SeisEIsobreL2;
        this.matrizKLocal[5][5] = CuatroEIsobreL;
    }


    private void setMatrizTDesplazamientos(){
        this.matrizTDesplazamientos[0][0] = lambdaX;
        this.matrizTDesplazamientos[0][1] = lambdaY;
        this.matrizTDesplazamientos[0][2] = 0;
        this.matrizTDesplazamientos[0][3] = 0;
        this.matrizTDesplazamientos[0][4] = 0;
        this.matrizTDesplazamientos[0][5] = 0;
        this.matrizTDesplazamientos[1][0] = -1 * lambdaY;
        this.matrizTDesplazamientos[1][1] = lambdaX;
        this.matrizTDesplazamientos[1][2] = 0;
        this.matrizTDesplazamientos[1][3] = 0;
        this.matrizTDesplazamientos[1][4] = 0;
        this.matrizTDesplazamientos[1][5] = 0;
        this.matrizTDesplazamientos[2][0] = 0;
        this.matrizTDesplazamientos[2][1] = 0;
        this.matrizTDesplazamientos[2][2] = 1;
        this.matrizTDesplazamientos[2][3] = 0;
        this.matrizTDesplazamientos[2][4] = 0;
        this.matrizTDesplazamientos[2][5] = 0;
        this.matrizTDesplazamientos[3][0] = 0;
        this.matrizTDesplazamientos[3][1] = 0;
        this.matrizTDesplazamientos[3][2] = 0;
        this.matrizTDesplazamientos[3][3] = lambdaX;
        this.matrizTDesplazamientos[3][4] = lambdaY;
        this.matrizTDesplazamientos[3][5] = 0;
        this.matrizTDesplazamientos[4][0] = 0;
        this.matrizTDesplazamientos[4][1] = 0;
        this.matrizTDesplazamientos[4][2] = 0;
        this.matrizTDesplazamientos[4][3] = -1 * lambdaY;
        this.matrizTDesplazamientos[4][4] = lambdaX;
        this.matrizTDesplazamientos[4][5] = 0;
        this.matrizTDesplazamientos[5][0] = 0;
        this.matrizTDesplazamientos[5][1] = 0;
        this.matrizTDesplazamientos[5][2] = 0;
        this.matrizTDesplazamientos[5][3] = 0;
        this.matrizTDesplazamientos[5][4] = 0;
        this.matrizTDesplazamientos[5][5] = 1;
    }


    private void setMatrizTFuerzas(){
        this.matrizTFuerzas[0][0] = lambdaX;
        this.matrizTFuerzas[0][1] = -1 * lambdaY;
        this.matrizTFuerzas[0][2] = 0;
        this.matrizTFuerzas[0][3] = 0;
        this.matrizTFuerzas[0][4] = 0;
        this.matrizTFuerzas[0][5] = 0;
        this.matrizTFuerzas[1][0] = lambdaY;
        this.matrizTFuerzas[1][1] = lambdaX;
        this.matrizTFuerzas[1][2] = 0;
        this.matrizTFuerzas[1][3] = 0;
        this.matrizTFuerzas[1][4] = 0;
        this.matrizTFuerzas[1][5] = 0;
        this.matrizTFuerzas[2][0] = 0;
        this.matrizTFuerzas[2][1] = 0;
        this.matrizTFuerzas[2][2] = 1;
        this.matrizTFuerzas[2][3] = 0;
        this.matrizTFuerzas[2][4] = 0;
        this.matrizTFuerzas[2][5] = 0;
        this.matrizTFuerzas[3][0] = 0;
        this.matrizTFuerzas[3][1] = 0;
        this.matrizTFuerzas[3][2] = 0;
        this.matrizTFuerzas[3][3] = lambdaX;
        this.matrizTFuerzas[3][4] = -1 * lambdaY;
        this.matrizTFuerzas[3][5] = 0;
        this.matrizTFuerzas[4][0] = 0;
        this.matrizTFuerzas[4][1] = 0;
        this.matrizTFuerzas[4][2] = 0;
        this.matrizTFuerzas[4][3] = lambdaY;
        this.matrizTFuerzas[4][4] = lambdaX;
        this.matrizTFuerzas[4][5] = 0;
        this.matrizTFuerzas[5][0] = 0;
        this.matrizTFuerzas[5][1] = 0;
        this.matrizTFuerzas[5][2] = 0;
        this.matrizTFuerzas[5][3] = 0;
        this.matrizTFuerzas[5][4] = 0;
        this.matrizTFuerzas[5][5] = 1;
    }



    private void setMatrizKGlobal(){
        setMatrizKLocal();
        setMatrizTDesplazamientos();
        setMatrizTFuerzas();
        double [][] matTemp = multiplicarMatrices(matrizTFuerzas, matrizKLocal);
        matrizKGlobal = multiplicarMatrices(matTemp,matrizTDesplazamientos);
    }


    private void setCondicionDeApoyodelElemento(){
        setCondicionDeApoyo(3);
        if((nodoN.esApoyo)&&(nodoF.esApoyo)){
            if ((nodoN.apoyo == 3)&&(nodoF.apoyo == 3)){
                setCondicionDeApoyo(3);
            } else if ((nodoN.apoyo == 3) && (nodoF.apoyo == 2)){
                setCondicionDeApoyo(2);
            } else if ((nodoN.apoyo == 2)&& (nodoF.apoyo == 3)){
                setCondicionDeApoyo(-2);
            }else if ((nodoN.apoyo == 2)&& (nodoF.apoyo == 2)){
                setCondicionDeApoyo(1);
            }
        }else if((nodoN.esApoyo)&&(!nodoF.esApoyo)){
            if ((nodoN.apoyo == 3)&&(nodoF.apoyo == 3)){
                setCondicionDeApoyo(3);
            } else if ((nodoN.apoyo == 2) && (nodoF.apoyo == 3)){
                setCondicionDeApoyo(-2);
            }
        }else if((!nodoN.esApoyo)&&(nodoF.esApoyo)){
            if ((nodoN.apoyo == 3)&&(nodoF.apoyo == 3)){
                setCondicionDeApoyo(3);
            } else if ((nodoN.apoyo == 3) && (nodoF.apoyo == 2)){
                setCondicionDeApoyo(2);
            }

        }else if((!nodoN.esApoyo)&&(!nodoF.esApoyo)){
            if ((nodoN.apoyo == 3)&&(nodoF.apoyo == 3)){
                setCondicionDeApoyo(3);
            }
        };

    };

    private void setCondicionDeApoyo(int condiAp){
        this.condicionDeApoyo = condiAp;
    }

    protected void agregarCarga( int condicionDeCarga, double magnitudDeCarga, double a, boolean sisInter){
        setCondicionDeApoyodelElemento();
        cargas.add(new Carga(getCondicionDeApoyo(),condicionDeCarga,L,a,magnitudDeCarga,getAnguloInclinacion(), sisInter));
    }

    private int getCondicionDeApoyo(){
        return this.condicionDeApoyo;
    }


    protected double getDeltaX (){
        return deltaX;
    }

    protected double getDeltaY (){
        return deltaY;
    }



    protected double getAnguloInclinacion(){
        return anguloInclinacion;
    }

    protected void setNodoN(Nodo n){
        nodoN = n;
    }

    protected void setNodoF(Nodo f){
        nodoF = f;
    }

    protected Nodo getNodoN(){
        return  nodoN;
    }

    protected Nodo getNodoF(){
        return nodoF;
    }

    protected void setDesplazamientos(double [][] des){
        desplazamientos = des;
    }

    protected double [][] getDesplazamientos(){
        return desplazamientos;
    }

    protected void setQNodos(double [][]qNod){
        qNodos = qNod;
    }

    protected double [][] getQNodos(){
        return qNodos;
    }

    protected int getId(){
        return id;
    }

    protected void setSeccionElemento (SeccionTransversal sec){
        seccionElemento = sec;
    }

    protected void setId(int i){
        id = i;
    }

    protected double getL(){return L;}


    protected double [][] getMatrizKLocal (){
        return matrizKLocal;
    }
    protected double [][] getMatrizTDesplazamientos (){
        return matrizTDesplazamientos;
    }
    protected double [][] getMatrizTFuerzas (){
        return matrizTFuerzas;
    }

    public double[][] multiplicarMatrices(double[][] mat1, double[][] mat2) {
        double[][] resultado = new double[mat1.length][mat2[0].length];
        // se comprueba si las matrices se pueden multiplicar
        if (mat1[0].length == mat2.length) {
            for (int i = 0; i < mat1.length; i++) {
                for (int j = 0; j < mat2[0].length; j++) {
                    for (int k = 0; k < mat1[0].length; k++) {
                        // aquí se multiplica la matriz
                        resultado[i][j] += mat1[i][k] * mat2[k][j];
                    }
                }
            }
        }
        return resultado;
    }


}
