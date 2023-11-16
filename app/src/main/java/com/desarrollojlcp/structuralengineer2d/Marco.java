package com.desarrollojlcp.structuralengineer2d;
import android.content.Context;
import android.widget.Toast;


import java.io.Serializable;
import java.util.Vector;

import Jama.LUDecomposition;
import Jama.Matrix;

public class Marco extends Object implements Serializable {
    //Vector que contendra los elementos que componen el marco actual
    static String fecha = "";
    protected int cantidadNodos = 0;
    protected int cantidadApoyos = 0;
    protected int cantidadElementos = 0;
    protected int cR   = 0;      //Cantidad de Reacciones
    protected int cRC  = 0;      //Contador de Reacciones Conocidas
    protected int cRNC = 0;      //Contador de Reacciones No Conocidas
    protected int contadorNodos = 0;
    protected int contadorSecciones = 0;
    protected int contadorElementos = 0;
    protected int contadorCargas = 0;
    protected Vector<Elemento> elementos = new Vector<Elemento>();
    protected Vector<Nodo> nodosNoApoyo = new Vector<Nodo>();
    protected Vector<Nodo> nodos = new Vector<Nodo>();
    protected Vector<Nodo> apoyos = new Vector<Nodo>();
    protected Vector<Nodo> apoyosEmpotrados = new Vector<Nodo>();
    protected Vector<Nodo> apoyosNoEmpotrados = new Vector<Nodo>();
    protected Vector<Nodo> nodosT = new Vector<Nodo>();
    protected Vector<Nodo> apoyosT = new Vector<Nodo>();
    protected Vector<SeccionTransversal> secciones = new Vector<SeccionTransversal>();
    protected double matrizKMarco[][];
    protected double matrizK11Marco[][];  //Matriz de rigidez de nodos
    protected double Qk[][];  //Vector de reacciones en los nodos
    protected double Qu[][];  //Vector de reaccionies en los apoyos
    protected double Du[][];  //Vector de recciones en los apoyos
    protected double matrizK21Marco[][];
    protected boolean yaCarga = false;
    protected boolean marcoAnalizado = false;
    protected boolean sistemaInternacional = true;
    protected boolean sistemaIngles = false;
    protected CharSequence [] unidades = new CharSequence[9];
    protected float xCen, yCen, displayScale;
    protected String rutaArchivo = "";
    protected boolean primeraPantalla;
    protected double maxXPos = 0;
    protected double maxXNeg = 0;
    protected double maxYPos = 0;
    protected double maxYNeg = 0;
    private String control[] = new String[16];


    public Marco() {
    }

    public Marco(int cantiNodos, int cantiApoyos, int cantiElementos) {
        cantidadNodos = cantiNodos;
        cantidadApoyos = cantiApoyos;
        cantidadElementos = cantiElementos;
        cR = (cantidadNodos + cantidadApoyos) * 3;      //contador de reacciones
    }

    protected void setSistemaUnidades(int sis){
        if (sis == 0){
            sistemaInternacional = true;
            sistemaIngles = false;
            unidades[0] = "(10^3) (mm2)";   //area
            unidades[1] = "(10^6) (mm4)";   //inercia
            unidades[2] = "(m)";            //distancia
            unidades[3] = "(kN)";            //fuerza puntual
            unidades[4] = "(GPa)";          //modulo elasticidad
            unidades[5] = "(kN-m)";         //momento
            unidades[6] = "(kN/m)";         //fuerza uniformemente distribuida
            unidades[8]= "(rad)";             //radianes
        } else if (sis == 1){
            sistemaInternacional = false;
            sistemaIngles = true;
            unidades[0] = "(in2)";          //area
            unidades[1] = "(in4)";          //inercia
            unidades[2] = "(ft)";           //distancia
            unidades[3] = "(KLb)";          //fuerza puntual
            unidades[4] = "(10^3) (Ksi)";   //modulo de elasticidad
            unidades[5] = "(KLb-in)";       //momento
            unidades[6] = "(KLb/ft)";       //fuerza uniformemente distribuida
            unidades[7] = "(in)";             //distancia para resultados Sistema Ingles
            unidades[8]= "(rad)";             //radianes
        }

        // set vector control a cero
        for (int i = 0; i < 16; i++) {
            this.control[i] = "0";
        }
    }

    protected void agregarNodo(Nodo n) {
        nodos.addElement(n);
    }

    protected void agregarElemento(Elemento elementoA) {
        elementos.addElement(elementoA);
    }


    protected void actualizarIdRecaccionesNodos() {
        for (int i = 0; i < nodos.size(); i++) {
            for (int j = 0; j < elementos.size(); j++) {
                if (nodos.elementAt(i).getId() == elementos.elementAt(j).getNodoN().getId()) {
                    elementos.elementAt(j).nodoN.setR1(nodos.elementAt(i).getR1());
                    elementos.elementAt(j).nodoN.setR2(nodos.elementAt(i).getR2());
                    elementos.elementAt(j).nodoN.setR3(nodos.elementAt(i).getR3());
                }
                if (nodos.elementAt(i).getId() == elementos.elementAt(j).getNodoF().getId()) {
                    elementos.elementAt(j).nodoF.setR1(nodos.elementAt(i).getR1());
                    elementos.elementAt(j).nodoF.setR2(nodos.elementAt(i).getR2());
                    elementos.elementAt(j).nodoF.setR3(nodos.elementAt(i).getR3());
                }
            }
        }
        for (int i = 0; i < apoyos.size(); i++) {
            for (int j = 0; j < elementos.size(); j++) {
                if (apoyos.elementAt(i).getId() == elementos.elementAt(j).getNodoN().getId()) {
                    elementos.elementAt(j).nodoN.setR1(apoyos.elementAt(i).getR1());
                    elementos.elementAt(j).nodoN.setR2(apoyos.elementAt(i).getR2());
                    elementos.elementAt(j).nodoN.setR3(apoyos.elementAt(i).getR3());
                }
                if (apoyos.elementAt(i).getId() == elementos.elementAt(j).getNodoF().getId()) {
                    elementos.elementAt(j).nodoF.setR1(apoyos.elementAt(i).getR1());
                    elementos.elementAt(j).nodoF.setR2(apoyos.elementAt(i).getR2());
                    elementos.elementAt(j).nodoF.setR3(apoyos.elementAt(i).getR3());
                }
            }
        }

        // Se realizo la actualizacion de id reacciones nodos
        this.control[0] = "1";
    }

    protected void analizarMarco(Vector<Nodo> nodosTV, Vector<Nodo> apoyosTV, int contadorReaccionesConocidas1, Vector<Nodo> apoyosNoEmpotrados1, Vector<Nodo> apoyosEmpotrados1) {

        cantidadNodos = nodos.size();
        nodosNoApoyo = nodosT;
        apoyos = apoyosT;
        apoyosT = apoyosTV;
        nodosT = nodosTV;
        actualizarIdRecaccionesNodos();
        apoyosEmpotrados   = apoyosEmpotrados1;
        apoyosNoEmpotrados = apoyosNoEmpotrados1;
        cR   = nodos.size()* 3;
        cRC  = contadorReaccionesConocidas1;
        cRNC = cR - cRC;
        matrizKMarco = new double[cR][cR];
        matrizK11Marco = new double[cRC][cRC];
        matrizK21Marco = new double[cRNC][cRC];
        Qk = new double[cRC][1];
        Du = new double[cRC][1];
        Qu = new double[cRNC][1];


        //se asigno cantidades de reaccion y se inicializo la matrizKMarco
        this.control[1] = "1";

        HiloArmarMatriz hiloArmarMatriz = new HiloArmarMatriz();
        hiloArmarMatriz.start();
        hiloArmarMatriz.setPriority(Thread.MAX_PRIORITY);
        try {
            hiloArmarMatriz.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HiloMatrizInversa hiloMatrizInversa = new HiloMatrizInversa();
        hiloMatrizInversa.start();
        hiloMatrizInversa.setPriority(Thread.MAX_PRIORITY);
        try {
            hiloMatrizInversa.join();
            //se armo la matriz K11 inversa del marco
            control[5] = "1";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Elemento elem = new Elemento();

        Qu = elem.multiplicarMatrices(matrizK21Marco, Du);
        //ser armo la matrizKMarco
        control[11] = "1";

        for (int i = 0; i < elementos.size(); i++) {

            //double rbnx, rbny,rbnm,rbfx,rbfy,rbfm;
            elementos.elementAt(i).rbnx = 0;
            elementos.elementAt(i).rbny = 0;
            elementos.elementAt(i).rbnm = 0;
            elementos.elementAt(i).rbfx = 0;
            elementos.elementAt(i).rbfy = 0;
            elementos.elementAt(i).rbfm = 0;

            for (int j = 0; j < elementos.elementAt(i).cargas.size(); j++) {
                elementos.elementAt(i).rbnx += elementos.elementAt(i).cargas.elementAt(j).rbnx;
                elementos.elementAt(i).rbny += elementos.elementAt(i).cargas.elementAt(j).rbny;
                elementos.elementAt(i).rbnm += elementos.elementAt(i).cargas.elementAt(j).rbnm;
                elementos.elementAt(i).rbfx += elementos.elementAt(i).cargas.elementAt(j).rbfx;
                elementos.elementAt(i).rbfy += elementos.elementAt(i).cargas.elementAt(j).rbfy;
                elementos.elementAt(i).rbfm += elementos.elementAt(i).cargas.elementAt(j).rbfm;
            }

            if ((elementos.elementAt(i).getNodoN().esApoyo)&& (elementos.elementAt(i).getNodoN().apoyo == 3)) {
                Nodo nodoTemporal = elementos.elementAt(i).nodoN;
                nodoTemporal.des1 = (0);
                nodoTemporal.des2 = (0);
                nodoTemporal.des3 = (0);
                elementos.elementAt(i).setNodoN(nodoTemporal);
            } else if ((elementos.elementAt(i).getNodoN().esApoyo)&& (elementos.elementAt(i).getNodoN().apoyo == 2)){
                Nodo nodoTemporal = elementos.elementAt(i).nodoN;
                nodoTemporal.des1 = (0);
                nodoTemporal.des2 = (0);
                nodoTemporal.des3 = (Du[elementos.elementAt(i).nodoN.r3][0]);
                elementos.elementAt(i).setNodoN(nodoTemporal);
            } else if ((!elementos.elementAt(i).getNodoN().esApoyo)&& (elementos.elementAt(i).getNodoN().apoyo == 3)) {
                Nodo nodoTemporal = elementos.elementAt(i).nodoN;
                nodoTemporal.des1 = (Du[elementos.elementAt(i).nodoN.r1][0]);
                nodoTemporal.des2 = (Du[elementos.elementAt(i).nodoN.r2][0]);
                nodoTemporal.des3 = (Du[elementos.elementAt(i).nodoN.r3][0]);
                elementos.elementAt(i).setNodoN(nodoTemporal);
            }

            if ((elementos.elementAt(i).getNodoF().esApoyo) && (elementos.elementAt(i).getNodoF().apoyo == 3)) {
                Nodo nodoTemporal = elementos.elementAt(i).nodoF;
                nodoTemporal.des1 = (0);
                nodoTemporal.des2 = (0);
                nodoTemporal.des3 = (0);
                elementos.elementAt(i).setNodoF(nodoTemporal);
            } else if ((elementos.elementAt(i).getNodoF().esApoyo) && (elementos.elementAt(i).getNodoF().apoyo == 2)) {
                Nodo nodoTemporal = elementos.elementAt(i).nodoF;
                nodoTemporal.des1 = (0);
                nodoTemporal.des2 = (0);
                nodoTemporal.des3 = (Du[elementos.elementAt(i).nodoF.r3][0]);
                elementos.elementAt(i).setNodoF(nodoTemporal);
            } else if ((!elementos.elementAt(i).getNodoF().esApoyo) && (elementos.elementAt(i).getNodoF().apoyo == 3)){
                Nodo nodoTemporal = elementos.elementAt(i).nodoF;
                nodoTemporal.des1 = (Du[elementos.elementAt(i).nodoF.r1][0]);
                nodoTemporal.des2 = (Du[elementos.elementAt(i).nodoF.r2][0]);
                nodoTemporal.des3 = (Du[elementos.elementAt(i).nodoF.r3][0]);
                elementos.elementAt(i).setNodoF(nodoTemporal);
            }
            //ser armo la matrizKMarco
            control[12] = "1";
            elementos.elementAt(i).desplazamientos[0][0] = elementos.elementAt(i).nodoN.des1;
            elementos.elementAt(i).desplazamientos[1][0] = elementos.elementAt(i).nodoN.des2;
            elementos.elementAt(i).desplazamientos[2][0] = elementos.elementAt(i).nodoN.des3;
            elementos.elementAt(i).desplazamientos[3][0] = elementos.elementAt(i).nodoF.des1;
            elementos.elementAt(i).desplazamientos[4][0] = elementos.elementAt(i).nodoF.des2;
            elementos.elementAt(i).desplazamientos[5][0] = elementos.elementAt(i).nodoF.des3;

            double matTemp[][] = elem.multiplicarMatrices(elementos.elementAt(i).matrizKLocal, elementos.elementAt(i).matrizTDesplazamientos);
            elementos.elementAt(i).setQNodos(elem.multiplicarMatrices(matTemp, elementos.elementAt(i).desplazamientos));

            //ser armo la matrizKMarco
            control[13] = "1";
            Nodo nodoNTemp = elementos.elementAt(i).nodoN;
            Nodo nodoFTemp = elementos.elementAt(i).nodoF;
            double[][] q_nodosTemp = elementos.elementAt(i).qNodos;
            nodoNTemp.rb1 = elementos.elementAt(i).rbnx;
            nodoNTemp.rb2 = elementos.elementAt(i).rbny;
            nodoNTemp.rb3 = elementos.elementAt(i).rbnm;
            nodoFTemp.rb1 = elementos.elementAt(i).rbfx;
            nodoFTemp.rb2 = elementos.elementAt(i).rbfy;
            nodoFTemp.rb3 = elementos.elementAt(i).rbfm;

            nodoNTemp.setRc(q_nodosTemp[0][0], q_nodosTemp[1][0], q_nodosTemp[2][0]);
            nodoFTemp.setRc(q_nodosTemp[3][0], q_nodosTemp[4][0], q_nodosTemp[5][0]);
            nodoNTemp.setRt();
            nodoFTemp.setRt();
            elementos.elementAt(i).nodoN = nodoNTemp;
            elementos.elementAt(i).nodoF = nodoFTemp;
        }
        apoyos = apoyosT;
        //ser armo la matrizKMarco
        control[14] = "1";

        for (int i = 0; i<elementos.size();i++){
            //aplicando formula q=k'TD
            elementos.elementAt(i).qInternas = elem.multiplicarMatrices(elem.multiplicarMatrices(elementos.elementAt(i).matrizKLocal, elementos.elementAt(i).matrizTDesplazamientos),elementos.elementAt(i).desplazamientos);
            //superponiendo cargas internas con carga FEM en cada elemento
            elementos.elementAt(i).qInternasF[0][0] = elementos.elementAt(i).qInternas[0][0] + elementos.elementAt(i).rbnx;
            elementos.elementAt(i).qInternasF[1][0] = elementos.elementAt(i).qInternas[1][0] + elementos.elementAt(i).rbny;
            elementos.elementAt(i).qInternasF[2][0] = elementos.elementAt(i).qInternas[2][0] + elementos.elementAt(i).rbnm;
            elementos.elementAt(i).qInternasF[3][0] = elementos.elementAt(i).qInternas[3][0] + elementos.elementAt(i).rbfx;
            elementos.elementAt(i).qInternasF[4][0] = elementos.elementAt(i).qInternas[4][0] + elementos.elementAt(i).rbfy;
            elementos.elementAt(i).qInternasF[5][0] = elementos.elementAt(i).qInternas[5][0] + elementos.elementAt(i).rbfm;

            //se rellena qFEM para imprimir reporte
            elementos.elementAt(i).qFEM[0][0] = elementos.elementAt(i).rbnx;
            elementos.elementAt(i).qFEM[1][0] = elementos.elementAt(i).rbny;
            elementos.elementAt(i).qFEM[2][0] = elementos.elementAt(i).rbnm;
            elementos.elementAt(i).qFEM[3][0] = elementos.elementAt(i).rbfx;
            elementos.elementAt(i).qFEM[4][0] = elementos.elementAt(i).rbfy;
            elementos.elementAt(i).qFEM[5][0] = elementos.elementAt(i).rbfm;
        }
        //se calculo la matrizInversaK11Marco
        control[15] = "1";
    }

    private class HiloArmarMatriz extends Thread {
        volatile boolean running = true;

        @Override
        public void run (){
            while (running)
            {
                try
                {
                    Thread.sleep(1000);// sleeps 1 second
                    //Do Your process here.
                    for (int i = 0; i < elementos.size(); i++) {

                        matrizKMarco[elementos.elementAt(i).nodoN.r1][elementos.elementAt(i).nodoN.r1] += elementos.elementAt(i).matrizKGlobal[0][0];

                        matrizKMarco[elementos.elementAt(i).nodoN.r1][elementos.elementAt(i).nodoN.r2] += elementos.elementAt(i).matrizKGlobal[0][1];

                        matrizKMarco[elementos.elementAt(i).nodoN.r1][elementos.elementAt(i).nodoN.r3] += elementos.elementAt(i).matrizKGlobal[0][2];

                        matrizKMarco[elementos.elementAt(i).nodoN.r1][elementos.elementAt(i).nodoF.r1] += elementos.elementAt(i).matrizKGlobal[0][3];

                        matrizKMarco[elementos.elementAt(i).nodoN.r1][elementos.elementAt(i).nodoF.r2] += elementos.elementAt(i).matrizKGlobal[0][4];

                        matrizKMarco[elementos.elementAt(i).nodoN.r1][elementos.elementAt(i).nodoF.r3] += elementos.elementAt(i).matrizKGlobal[0][5];

                        matrizKMarco[elementos.elementAt(i).nodoN.r2][elementos.elementAt(i).nodoN.r1] += elementos.elementAt(i).matrizKGlobal[1][0];

                        matrizKMarco[elementos.elementAt(i).nodoN.r2][elementos.elementAt(i).nodoN.r2] += elementos.elementAt(i).matrizKGlobal[1][1];

                        matrizKMarco[elementos.elementAt(i).nodoN.r2][elementos.elementAt(i).nodoN.r3] += elementos.elementAt(i).matrizKGlobal[1][2];

                        matrizKMarco[elementos.elementAt(i).nodoN.r2][elementos.elementAt(i).nodoF.r1] += elementos.elementAt(i).matrizKGlobal[1][3];

                        matrizKMarco[elementos.elementAt(i).nodoN.r2][elementos.elementAt(i).nodoF.r2] += elementos.elementAt(i).matrizKGlobal[1][4];

                        matrizKMarco[elementos.elementAt(i).nodoN.r2][elementos.elementAt(i).nodoF.r3] += elementos.elementAt(i).matrizKGlobal[1][5];

                        matrizKMarco[elementos.elementAt(i).nodoN.r3][elementos.elementAt(i).nodoN.r1] += elementos.elementAt(i).matrizKGlobal[2][0];

                        matrizKMarco[elementos.elementAt(i).nodoN.r3][elementos.elementAt(i).nodoN.r2] += elementos.elementAt(i).matrizKGlobal[2][1];

                        matrizKMarco[elementos.elementAt(i).nodoN.r3][elementos.elementAt(i).nodoN.r3] += elementos.elementAt(i).matrizKGlobal[2][2];

                        matrizKMarco[elementos.elementAt(i).nodoN.r3][elementos.elementAt(i).nodoF.r1] += elementos.elementAt(i).matrizKGlobal[2][3];

                        matrizKMarco[elementos.elementAt(i).nodoN.r3][elementos.elementAt(i).nodoF.r2] += elementos.elementAt(i).matrizKGlobal[2][4];

                        matrizKMarco[elementos.elementAt(i).nodoN.r3][elementos.elementAt(i).nodoF.r3] += elementos.elementAt(i).matrizKGlobal[2][5];

                        matrizKMarco[elementos.elementAt(i).nodoF.r1][elementos.elementAt(i).nodoN.r1] += elementos.elementAt(i).matrizKGlobal[3][0];

                        matrizKMarco[elementos.elementAt(i).nodoF.r1][elementos.elementAt(i).nodoN.r2] += elementos.elementAt(i).matrizKGlobal[3][1];

                        matrizKMarco[elementos.elementAt(i).nodoF.r1][elementos.elementAt(i).nodoN.r3] += elementos.elementAt(i).matrizKGlobal[3][2];

                        matrizKMarco[elementos.elementAt(i).nodoF.r1][elementos.elementAt(i).nodoF.r1] += elementos.elementAt(i).matrizKGlobal[3][3];

                        matrizKMarco[elementos.elementAt(i).nodoF.r1][elementos.elementAt(i).nodoF.r2] += elementos.elementAt(i).matrizKGlobal[3][4];

                        matrizKMarco[elementos.elementAt(i).nodoF.r1][elementos.elementAt(i).nodoF.r3] += elementos.elementAt(i).matrizKGlobal[3][5];

                        matrizKMarco[elementos.elementAt(i).nodoF.r2][elementos.elementAt(i).nodoN.r1] += elementos.elementAt(i).matrizKGlobal[4][0];

                        matrizKMarco[elementos.elementAt(i).nodoF.r2][elementos.elementAt(i).nodoN.r2] += elementos.elementAt(i).matrizKGlobal[4][1];

                        matrizKMarco[elementos.elementAt(i).nodoF.r2][elementos.elementAt(i).nodoN.r3] += elementos.elementAt(i).matrizKGlobal[4][2];

                        matrizKMarco[elementos.elementAt(i).nodoF.r2][elementos.elementAt(i).nodoF.r1] += elementos.elementAt(i).matrizKGlobal[4][3];

                        matrizKMarco[elementos.elementAt(i).nodoF.r2][elementos.elementAt(i).nodoF.r2] += elementos.elementAt(i).matrizKGlobal[4][4];

                        matrizKMarco[elementos.elementAt(i).nodoF.r2][elementos.elementAt(i).nodoF.r3] += elementos.elementAt(i).matrizKGlobal[4][5];

                        matrizKMarco[elementos.elementAt(i).nodoF.r3][elementos.elementAt(i).nodoN.r1] += elementos.elementAt(i).matrizKGlobal[5][0];

                        matrizKMarco[elementos.elementAt(i).nodoF.r3][elementos.elementAt(i).nodoN.r2] += elementos.elementAt(i).matrizKGlobal[5][1];

                        matrizKMarco[elementos.elementAt(i).nodoF.r3][elementos.elementAt(i).nodoN.r3] += elementos.elementAt(i).matrizKGlobal[5][2];

                        matrizKMarco[elementos.elementAt(i).nodoF.r3][elementos.elementAt(i).nodoF.r1] += elementos.elementAt(i).matrizKGlobal[5][3];

                        matrizKMarco[elementos.elementAt(i).nodoF.r3][elementos.elementAt(i).nodoF.r2] += elementos.elementAt(i).matrizKGlobal[5][4];

                        matrizKMarco[elementos.elementAt(i).nodoF.r3][elementos.elementAt(i).nodoF.r3] += elementos.elementAt(i).matrizKGlobal[5][5];
                    }
                    //ser actualizo la matrizKMarco
                    control[2] = "1";
                    //se inicializo matrizK11Marco
                    control[3] = "1";
                    for (int i = 0; i < (cRC); i++) {
                        for (int j = 0; j < (cRC); j++) {
                            matrizK11Marco[i][j] = matrizKMarco[i][j];
                        }
                    }
                    //ser armo la matrizKMarco
                    control[4] = "1";

                    for (int i = 0; i < cRNC; i++) {
                        for (int j = 0; j < (cRC); j++) {
                            matrizK21Marco[i][j] = matrizKMarco[i + cRC][j];
                        }
                    }
                    int contadorReaccionesNodos = 0;
                    for (int i = 0; i < nodosT.size(); i++) {
                        Qk[contadorReaccionesNodos][0] = nodosT.elementAt(i).ra1;
                        contadorReaccionesNodos += 1;
                        Qk[contadorReaccionesNodos][0] = nodosT.elementAt(i).ra2;
                        contadorReaccionesNodos += 1;
                        Qk[contadorReaccionesNodos][0] = nodosT.elementAt(i).ra3;
                        contadorReaccionesNodos += 1;
                    }

                    if (cRC > (nodosT.size() * 3)){
                        for (int i = 0; i < apoyosNoEmpotrados.size(); i++) {
                            Qk[contadorReaccionesNodos][0] = apoyosNoEmpotrados.elementAt(i).ra3;
                            contadorReaccionesNodos += 1;
                        }
                    }

                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                running = false;
            }
            if (!running) return;

        }
    }



    private class HiloMatrizInversa extends Thread {
        volatile boolean running = true;

        @Override
        public void run (){
            while (running)
            {
                try
                {
                    Thread.sleep(1000);// sleeps 1 second
                    //Do Your process here.
                    //se inicializo matrizK11Marco
                    Matrix a = new Matrix(matrizK11Marco);
                    LUDecomposition luDecomposition = new LUDecomposition(a);
                    Matrix b = new Matrix(Qk);
                    Matrix x = luDecomposition.solve(b); // solve Ax = b for the unknown vector x
                    Du = x.getArray();
                    Matrix residual = a.times(x).minus(b); // calculate the residual error
                    double rnorm = residual.normInf(); // get the max error (yes, it's very small)

                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                running = false;
            }
            if (!running) return;

        }
    }


    protected void setContadorNodos (int num){
        this.contadorNodos = num;
    }
    protected  int getContadorNodos (){
        return this.contadorNodos;
    }
    protected Vector<Nodo> getNodos() {
        return this.nodos;
    }
    protected void setNodos(Vector<Nodo> nodos2) {
        this.nodos = nodos2;
    }
    protected int getContadorSecciones (){
        return contadorSecciones;
    }
    protected void setContadorSecciones(int contSec){
        contadorSecciones = contSec;
    }
    protected void setContadorElementos (int contElem){
        contadorElementos = contElem;
    }
    protected int getContadorElementos(){
        return contadorElementos;
    }
    public double getMaxXPos(){
        return this.maxXPos;
    }
    public double getMaxXNeg(){
        return this.maxXNeg;
    }
    public double getMaxYPos(){
        return this.maxYPos;
    }
    public double getMaxYNeg(){
        return this.maxYNeg;
    }
    public void setMaxXPos(double val){
        this.maxXPos = val;
    }
    public void setMaxXNeg(double val){
        this.maxXNeg = val;
    }
    public void setMaxYPos(double val){
        this.maxYPos = val;
    }
    public void setMaxYNeg(double val){
        this.maxYNeg = val;
    }
    protected double [][] getQk(){
        return this.Qk;
    }
    private void setQk ( double [][] Qk1){
        this.Qk = Qk1;
    }


    public double[][] matrizInversa(double[][] matriz) {
        double det=1/determinante(matriz);
        double[][] nmatriz=matrizAdjunta(matriz);
        multiplicarMatriz(det,nmatriz);
        return nmatriz;
    }
    public void multiplicarMatriz(double n, double[][] matriz) {
        for(int i=0;i<matriz.length;i++)
            for(int j=0;j<matriz.length;j++)
                matriz[i][j]*=n;
    }
    public  double[][] matrizAdjunta(double [][] matriz){
        return matrizTranspuesta(matrizCofactores(matriz));
    }
    public  double[][] matrizCofactores(double[][] matriz){
        double[][] nm=new double[matriz.length][matriz.length];
        for(int i=0;i<matriz.length;i++) {
            for(int j=0;j<matriz.length;j++) {
                double[][] det=new double[matriz.length-1][matriz.length-1];
                double detValor;
                for(int k=0;k<matriz.length;k++) {
                    if(k!=i) {
                        for(int l=0;l<matriz.length;l++) {
                            if(l!=j) {
                                int indice1=k<i ? k : k-1 ;
                                int indice2=l<j ? l : l-1 ;
                                det[indice1][indice2]=matriz[k][l];
                            }
                        }
                    }
                }
                detValor=determinante(det);
                nm[i][j]=detValor * (double)Math.pow(-1, i+j+2);
            }
        }
        return nm;
    }
    public  double[][] matrizTranspuesta(double [][] matriz){
        double[][]nuevam=new double[matriz[0].length][matriz.length];
        for(int i=0; i<matriz.length; i++)
        {
            for(int j=0; j<matriz.length; j++)
                nuevam[i][j]=matriz[j][i];
        }
        return nuevam;
    }
    public  double determinante(double[][] matriz) {
        double det;
        if(matriz.length==2)
        {
            det=(matriz[0][0]*matriz[1][1])-(matriz[1][0]*matriz[0][1]);
            return det;
        }
        double suma=0;
        for(int i=0; i<matriz.length; i++){
            double[][] nm=new double[matriz.length-1][matriz.length-1];
            for(int j=0; j<matriz.length; j++){
                if(j!=i){
                    for(int k=1; k<matriz.length; k++){
                        int indice=-1;
                        if(j<i)
                            indice=j;
                        else if(j>i)
                            indice=j-1;
                        nm[indice][k-1]=matriz[j][k];
                    }
                }
            }
            if(i%2==0)
                suma+=matriz[i][0] * determinante(nm);
            else
                suma-=matriz[i][0] * determinante(nm);
        }
        return suma;
    }
}
