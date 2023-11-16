package com.desarrollojlcp.structuralengineer2d;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;



public class Ingreso2 extends AppCompatActivity implements View.OnClickListener {
    private VistaTrabajo vistaTrabajo;
    private Marco marcoActual;
    String cadi = "// ";
    Vector<Nodo> nodosT  = new Vector<Nodo>();
    Vector<Nodo> apoyosT = new Vector<Nodo>();
    int cantidadReaccionesConocidas;
    Vector<Nodo> apoyosEmpotrados = new Vector<Nodo>();
    Vector<Nodo> apoyosNoEmpotrados = new Vector<Nodo>();
    String fechaNombreReporte="";
    StringBuilder sb = new StringBuilder();
    int cantidadNodos = 0;
    private boolean videoCargado = false;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso2);
        vistaTrabajo = (VistaTrabajo) findViewById(R.id.vista_trabajo);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        marcoActual = (Marco) getIntent().getExtras().getSerializable("marco_trabajo");
        vistaTrabajo.actualizarVistaTrabajo(marcoActual);

        if (marcoActual.getNodos().size() > 0) {
            for (int i = 0; i < marcoActual.getNodos().size(); i++) {
                Nodo nodoTemp = marcoActual.getNodos().elementAt(i);
                cadi = cadi + String.valueOf( marcoActual.getNodos().elementAt(i).idNodo) + " / " + String.valueOf( nodoTemp.coordenadaX) +" / "+ String.valueOf( nodoTemp.coordenadaY)+" // ";
            }
        }

        FloatingActionButton botonCentrar = findViewById(R.id.boton_centrar);
        FloatingActionButton botonInfo = findViewById(R.id.boton_info);
        FloatingActionButton botonAgregarSeccion = findViewById(R.id.boton_agregar_secTrans);
        FloatingActionButton botonAgregarNodo = findViewById(R.id.boton_agregar_nodo);
        FloatingActionButton botonAgregarElemento = findViewById(R.id.boton_agregar_elemento);
        FloatingActionButton botonAgregarCarga= findViewById(R.id.boton_agregar_carga);
        FloatingActionButton botonAnalizar= findViewById(R.id.boton_analizar);

        //colores iniciales
        botonAgregarSeccion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonesFAB)));
        botonAgregarNodo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));
        botonAgregarElemento.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));
        botonAgregarCarga.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));
        botonAnalizar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));

        //Colores segun condicion del marco Actual
        if (marcoActual.secciones.size()>0){
            botonAgregarNodo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonesFAB)));
        }
        if ((marcoActual.secciones.size() > 0) && (marcoActual.getNodos().size() > 1)){
            botonAgregarElemento.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonesFAB)));
        }
        if (marcoActual.elementos.size()>1){
            botonAgregarCarga.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonesFAB)));
        }



        if (marcoActual.yaCarga){
            botonAnalizar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRun)));
        }

        // Listenes de los botones
        botonCentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = 125;
                marcoActual.yCen = 900;
                marcoActual.displayScale = 4.0f;
                vistaTrabajo.actualizarVistaTrabajo(marcoActual);
            }
        });

        botonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                Intent intent = new Intent(getApplicationContext(), Info.class);
                intent.putExtra("marco_trabajo", marcoActual);
                finish();
                startActivity(intent);
            }
        });

        botonAgregarSeccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                Intent intent = new Intent(getApplicationContext(), IngresoSeccion.class);
                intent.putExtra("marco_trabajo", marcoActual);
                finish();
                startActivity(intent);
            }
        });

        botonAgregarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marcoActual.contadorNodos > 8){
                    String cadi = getResources().getText(R.string.pague).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                }else {
                    marcoActual.xCen = vistaTrabajo.xCen;
                    marcoActual.yCen = vistaTrabajo.yCen;
                    marcoActual.displayScale = vistaTrabajo.displayScale;
                    if (marcoActual.secciones.size()>0){
                        Intent intent = new Intent(getApplicationContext(), IngresoNodo.class);
                        intent.putExtra("marco_trabajo", marcoActual);
                        finish();
                        startActivity(intent);
                    } else {
                        String cadi = getResources().getText(R.string.pague).toString();
                        Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        botonAgregarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                if ((marcoActual.secciones.size() > 0) && (marcoActual.getNodos().size() > 2)){
                    Intent intent = new Intent(getApplicationContext(), IngresoElemento.class);
                    intent.putExtra("marco_trabajo", marcoActual);
                    finish();
                    startActivity(intent);
                }else {
                    String cadi = getResources().getText(R.string.minimos_para_crear_elemento).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                }
            }
        });


        botonAgregarCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                if (marcoActual.elementos.size() >1){
                    Intent intent = new Intent(getApplicationContext(), IngresoCarga.class);
                    intent.putExtra("marco_trabajo", marcoActual);
                    finish();
                    startActivity(intent);
                }else {
                    String cadi = getResources().getText(R.string.minimos_para_crear_carga).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analizar();

                try{
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error when analyzing, please take a screenshot and send it to the developer: " + e.toString(),Toast.LENGTH_LONG).show();
                }

            }
            });
    }

    private void analizar(){
        marcoActual.xCen = vistaTrabajo.xCen;
        marcoActual.yCen = vistaTrabajo.yCen;
        marcoActual.displayScale = vistaTrabajo.displayScale;
        if (marcoActual.yaCarga){

            //reducir cargas FEM y aplicada en cada nodo, segun condicion de carga aplicada
            for (int i = 0; i < marcoActual.nodos.size();i++){
                Nodo nodoTemp= marcoActual.nodos.elementAt(i);
                nodoTemp.rb1 = 0;
                nodoTemp.rb2 = 0;
                nodoTemp.rb3 = 0;
                nodoTemp.ra1 = 0;
                nodoTemp.ra2 = 0;
                nodoTemp.ra3 = 0;
                for (int j = 0; j < marcoActual.elementos.size();j++){
                    if (marcoActual.elementos.elementAt(j).cargas.size() > 0){
                        for (int k = 0; k < marcoActual.elementos.elementAt(j).cargas.size();k++){
                            if (nodoTemp.idNodo == marcoActual.elementos.elementAt(j).nodoN.idNodo){
                                nodoTemp.rb1 += marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbnx;
                                nodoTemp.rb2 += marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbny;
                                nodoTemp.rb3 += marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbnm;
                                nodoTemp.ra1 += (-1 * marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbnx);
                                nodoTemp.ra2 += (-1 * marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbny);
                                nodoTemp.ra3 += (-1 * marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbnm);
                            } else if (nodoTemp.idNodo == marcoActual.elementos.elementAt(j).nodoF.idNodo){
                                nodoTemp.rb1 += marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbfx;
                                nodoTemp.rb2 += marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbfy;
                                nodoTemp.rb3 += marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbfm;
                                nodoTemp.ra1 += (-1 * marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbfx);
                                nodoTemp.ra2 += (-1 * marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbfy);
                                nodoTemp.ra3 += (-1 * marcoActual.elementos.elementAt(j).cargas.elementAt(k).rbfm);
                            }
                        }



                    }
                }
                marcoActual.nodos.setElementAt(nodoTemp , i);

                if (nodoTemp.getCoordenadaX() > marcoActual.getMaxXPos()){
                    marcoActual.setMaxXPos(nodoTemp.getCoordenadaX());
                } else if(nodoTemp.getCoordenadaX() < marcoActual.getMaxXNeg()){
                    marcoActual.setMaxXNeg(nodoTemp.getCoordenadaX());
                } else if (nodoTemp.getCoordenadaX() == marcoActual.getMaxXPos()){
                    marcoActual.setMaxXPos(nodoTemp.getCoordenadaX());
                }else if (nodoTemp.getCoordenadaX() == marcoActual.getMaxXNeg()){
                    marcoActual.setMaxXNeg(nodoTemp.getCoordenadaX());
                }

                if (nodoTemp.getCoordenadaY() > marcoActual.getMaxYPos()){
                    marcoActual.setMaxYPos(nodoTemp.getCoordenadaY());
                } else if (nodoTemp.getCoordenadaY() < marcoActual.getMaxYNeg()){
                    marcoActual.setMaxYNeg(nodoTemp.getCoordenadaY());
                }else if (nodoTemp.getCoordenadaY() == marcoActual.getMaxYPos()){
                    marcoActual.setMaxYPos(nodoTemp.getCoordenadaY());
                }else if (nodoTemp.getCoordenadaY() == marcoActual.getMaxYNeg()){
                    marcoActual.setMaxYNeg(nodoTemp.getCoordenadaY());
                }

            }

            //Se clasifican los nodos entre apoyos y nodos.  Tambien se clasifican nodos entre empotrados y no empotrados


            apoyosEmpotrados.clear();
            apoyosNoEmpotrados.clear();
            apoyosT.clear();
            nodosT.clear();


            for (int i = 0; i < marcoActual.nodos.size();i++){
                if ((marcoActual.nodos.elementAt(i).apoyo == 3)&&(marcoActual.nodos.elementAt(i).esApoyo)) {
                    apoyosEmpotrados.addElement(marcoActual.nodos.elementAt(i));
                    apoyosT.add(marcoActual.nodos.elementAt(i));
                } else if ((marcoActual.nodos.elementAt(i).apoyo == 2)&&(marcoActual.nodos.elementAt(i).esApoyo)) {
                    apoyosNoEmpotrados.addElement(marcoActual.nodos.elementAt(i));
                    apoyosT.add(marcoActual.nodos.elementAt(i));
                }  else if ((marcoActual.nodos.elementAt(i).apoyo == 3)&&(!marcoActual.nodos.elementAt(i).esApoyo)) {
                    nodosT.addElement(marcoActual.nodos.elementAt(i));
                }
            }

            int contadorReacciones = 0;
            cantidadReaccionesConocidas = 0;

            //Se asignan id reacciones a nodos
            for (int i = 0; i < nodosT.size(); i++){
                nodosT.elementAt(i).setR1(contadorReacciones);
                contadorReacciones += 1;
                nodosT.elementAt(i).setR2(contadorReacciones);
                contadorReacciones += 1;
                nodosT.elementAt(i).setR3(contadorReacciones);
                contadorReacciones += 1;
                cantidadReaccionesConocidas = contadorReacciones;
            }

            //Si existen apoyos no empotrados
            if (apoyosNoEmpotrados.size() > 0){
                //Se asignan id reacciones a momentos en apoyos no empotrados (para formar matriz k del marco)
                for (int i = 0; i < apoyosNoEmpotrados.size(); i++){
                    apoyosNoEmpotrados.elementAt(i).setR3(contadorReacciones);
                    for (int j = 0; j <apoyosT.size();j++){
                        if (apoyosNoEmpotrados.elementAt(i).idNodo == apoyosT.elementAt(j).idNodo){
                            apoyosT.elementAt(j).setR3(contadorReacciones);
                        }
                    }
                    contadorReacciones += 1;
                    cantidadReaccionesConocidas = contadorReacciones;
                }

                //Se asignan id reacciones al resto de reacciones en apoyos no empotrados
                //Tambien se agrega el nodo no empotrado al vector apoyosT
                for (int i = 0; i < apoyosNoEmpotrados.size(); i++){
                    apoyosNoEmpotrados.elementAt(i).setR1(contadorReacciones);
                    for (int j = 0; j <apoyosT.size();j++){
                        if (apoyosNoEmpotrados.elementAt(i).idNodo == apoyosT.elementAt(j).idNodo){
                            apoyosT.elementAt(j).setR1(contadorReacciones);
                        }
                    }
                    contadorReacciones += 1;

                    apoyosNoEmpotrados.elementAt(i).setR2(contadorReacciones);
                    for (int j = 0; j <apoyosT.size();j++){
                        if (apoyosNoEmpotrados.elementAt(i).idNodo == apoyosT.elementAt(j).idNodo){
                            apoyosT.elementAt(j).setR2(contadorReacciones);
                        }
                    }
                    contadorReacciones += 1;

                }
            }

            //Se asignan id reacciones a los apoyos empotrados
            //Tambien se agrega el nodo empotrado al vector apoyosT
            for (int i = 0; i < apoyosEmpotrados.size(); i++){
                apoyosEmpotrados.elementAt(i).setR1(contadorReacciones);
                for (int j = 0; j <apoyosT.size();j++){
                    if (apoyosEmpotrados.elementAt(i).idNodo == apoyosT.elementAt(j).idNodo){
                        apoyosT.elementAt(j).setR1(contadorReacciones);
                    }
                }
                contadorReacciones += 1;
                apoyosEmpotrados.elementAt(i).setR2(contadorReacciones);
                for (int j = 0; j <apoyosT.size();j++){
                    if (apoyosEmpotrados.elementAt(i).idNodo == apoyosT.elementAt(j).idNodo){
                        apoyosT.elementAt(j).setR2(contadorReacciones);
                    }
                }
                contadorReacciones += 1;
                apoyosEmpotrados.elementAt(i).setR3(contadorReacciones);
                for (int j = 0; j <apoyosT.size();j++){
                    if (apoyosEmpotrados.elementAt(i).idNodo == apoyosT.elementAt(j).idNodo){
                        apoyosT.elementAt(j).setR3(contadorReacciones);
                    }
                }
                contadorReacciones += 1;
            }



            //marcoActual.cR = contadorReacciones;
            for (int i = 0; i < marcoActual.nodos.size();i++){
                for (int j = 0; j < nodosT.size(); j++){
                    if (marcoActual.nodos.elementAt(i).getId() == nodosT.elementAt(j).getId() ){
                        marcoActual.nodos.elementAt(i).setR1(nodosT.elementAt(j).getR1());
                        marcoActual.nodos.elementAt(i).setR2(nodosT.elementAt(j).getR2());
                        marcoActual.nodos.elementAt(i).setR3(nodosT.elementAt(j).getR3());
                    }
                }
                for (int j = 0; j < apoyosT.size(); j++){
                    if (marcoActual.nodos.elementAt(i).getId() == apoyosT.elementAt(j).getId() ){
                        marcoActual.nodos.elementAt(i).setR1(apoyosT.elementAt(j).getR1());
                        marcoActual.nodos.elementAt(i).setR2(apoyosT.elementAt(j).getR2());
                        marcoActual.nodos.elementAt(i).setR3(apoyosT.elementAt(j).getR3());
                    }
                }
            }
            marcoActual.actualizarIdRecaccionesNodos();
            marcoActual.cantidadNodos = nodosT.size();
            marcoActual.cantidadApoyos = apoyosT.size();
            marcoActual.cantidadElementos = marcoActual.elementos.size();


            //Se analiza el marco, con valores calculados anteriormente

            marcoActual.analizarMarco(nodosT,apoyosT, cantidadReaccionesConocidas,apoyosNoEmpotrados,apoyosEmpotrados);
            marcoActual.marcoAnalizado = true;
            vistaTrabajo.actualizarVistaTrabajo(marcoActual);
            vistaTrabajo.guardarImagen();
            generarTXT();
            Toast.makeText(getApplicationContext(),getResources().getText(R.string.marco_analizado).toString(),Toast.LENGTH_LONG).show();


        } else {
            String cadi = getResources().getText(R.string.minimos_para_crear_analizar).toString();
            Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        vistaTrabajo.invalidate();
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getText(R.string.cerrando_app))
                .setMessage(getResources().getText(R.string.seguro_cerrar_app))
                .setPositiveButton(getResources().getText(R.string.si), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(getResources().getText(R.string.no), null)
                .show();
    }

    public void generarTXT(){
        final String fecha1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        String nomCarpeta = "Reports - StructuralAnalysis2d";
        String ano = fecha1.substring(17,19);
        String mes = fecha1.substring(12,14);
        String dia = fecha1.substring(9,11);
        String hor = fecha1.substring(0,2);
        String min = fecha1.substring(3,5);
        fechaNombreReporte = ano+mes+dia+hor+min;
        fechaNombreReporte.replace(" ","");
        String rutaCarpetaActual = tarjetaSD+ File.separator + nomCarpeta;
        String nombreArchivoTXT = fechaNombreReporte+".txt";
        String rutaArchivoTXT = rutaCarpetaActual + File.separator + nombreArchivoTXT;

        File file;

        Uri uri = Uri.parse(rutaArchivoTXT);
        file = new File(uri.getPath());
        file.getParentFile().mkdirs();
        FileOutputStream pw = null;
        try {
            pw = new FileOutputStream(file);
            HiloGenerarArchivoTexto hiloGenerarArchivoTexto = new HiloGenerarArchivoTexto();
            hiloGenerarArchivoTexto.start();
            hiloGenerarArchivoTexto.join();
            byte[] contenido = sb.toString().getBytes();
            pw.write(contenido);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error FileNotFound -----  "+ e +"  -----on loading TXT, working on this.",Toast.LENGTH_LONG).show();
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this,"Error-----  "+ e1.toString() +"  -----on loading TXT, working on this.",Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






    private class HiloGenerarArchivoTexto extends Thread {
        volatile boolean running = true;

        @Override
        public void run (){
            while (running)
            {
                try
                {
                    Thread.sleep(1000);// sleeps 1 second
                    //Do Your process here.
                    DecimalFormat df = new DecimalFormat("%6.2e");
                    String cadTemp = "";
                    sb = new StringBuilder();

                    //Cabecera del reporte
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.titulo_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.fecha) + fechaNombreReporte);
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");


                    //Secciones
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.datos_seccion));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    for (int i = 0; i < (marcoActual.secciones.size()); i++) {
                        sb.append(getResources().getText(R.string.ingreso_id_sec_t) + ": " + marcoActual.secciones.elementAt(i).id);
                        sb.append("\r\n");

                        if (marcoActual.sistemaInternacional){
                            sb.append(getResources().getText(R.string.modulo_gpa).toString()+" "+ marcoActual.unidades[4] + ": " + marcoActual.secciones.elementAt(i).moduloElasticidadGPA);
                            sb.append("\r\n");
                            sb.append(getResources().getText(R.string.inercia_cm).toString()+" "+ marcoActual.unidades[1] + ": " + marcoActual.secciones.elementAt(i).inercia*1000000);
                            sb.append("\r\n");
                            sb.append( getResources().getText(R.string.area_cm ).toString() +" "+ marcoActual.unidades[0] + ": " + marcoActual.secciones.elementAt(i).area*1000);

                        }else {
                            sb.append(getResources().getText(R.string.modulo_gpa).toString()+" "+ marcoActual.unidades[4] + ": " + marcoActual.secciones.elementAt(i).moduloKsi);
                            sb.append("\r\n");
                            sb.append(getResources().getText(R.string.inercia_cm).toString()+" "+ marcoActual.unidades[1] + ": " + marcoActual.secciones.elementAt(i).inercia);
                            sb.append("\r\n");
                            sb.append( getResources().getText(R.string.area_cm ).toString() +" "+ marcoActual.unidades[0] + ": " + marcoActual.secciones.elementAt(i).area);
                        }
                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                        sb.append("\r\n");
                    }
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");


                    //Elementos
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.datos_elemento));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    for (int i = 0; i < (marcoActual.elementos.size()); i++) {
                        sb.append(getResources().getText(R.string.ingreso_id_elemento) + ": " + marcoActual.elementos.elementAt(i).getId());
                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.ingreso_id_sec_t) + ": " + marcoActual.elementos.elementAt(i).seccionElemento.id);
                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.nodo_n) + String.valueOf(marcoActual.elementos.elementAt(i).nodoN.getId()) + "    ");
                        sb.append(getResources().getText(R.string.ingreso_nodo_cx) +": "+ String.valueOf(marcoActual.elementos.elementAt(i).nodoN.getCoordenadaX()) + "    ");
                        sb.append(getResources().getText(R.string.ingreso_nodo_cy) +": "+ String.valueOf(marcoActual.elementos.elementAt(i).nodoN.getCoordenadaY()));
                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.nodo_f) + String.valueOf(marcoActual.elementos.elementAt(i).nodoF.getId()) + "    ");
                        sb.append(getResources().getText(R.string.ingreso_nodo_cx) +": "+ String.valueOf(marcoActual.elementos.elementAt(i).nodoF.getCoordenadaX()) + "    ");
                        sb.append(getResources().getText(R.string.ingreso_nodo_cy) +": "+ String.valueOf(marcoActual.elementos.elementAt(i).nodoF.getCoordenadaY()));
                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.longitud) + String.valueOf(marcoActual.elementos.elementAt(i).getL()) + "    ");
                        sb.append(getResources().getText(R.string.delta_x) + String.valueOf(marcoActual.elementos.elementAt(i).lambdaX) + "    ");
                        sb.append(getResources().getText(R.string.delta_y) + String.valueOf(marcoActual.elementos.elementAt(i).lambdaY));
                        sb.append("\r\n");
                        sb.append("\r\n");


                        String ajMat;


                        sb.append(getResources().getText(R.string.matriz_k_global));
                        sb.append("\r\n");
                        String [][] idReac = new String[6][1];
                        for (int j = 0; j < 6; j++) {
                            if ((j == 0)) {
                                sb.append("      " + String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r1));
                                idReac[0][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r1);
                                sb.append("             " + String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r2));
                                idReac[1][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r2);
                                sb.append("             " + String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r3));
                                idReac[2][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r3);
                                sb.append("             " + String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r1));
                                idReac[3][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r1);
                                sb.append("             " + String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r2));
                                idReac[4][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r2);
                                sb.append("             " + String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r3));
                                idReac[5][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r3);
                                sb.append("\r\n");
                            }
                        }

                        for (int m = 0; m < 6; m++) {
                            for (int k = 0; k < 6; k++) {
                                if (marcoActual.elementos.elementAt(i).matrizKGlobal[m][k] < 0) {
                                    ajMat = "";
                                } else {
                                    ajMat = " ";
                                }
                                sb.append(ajMat + String.format("%6.3e", marcoActual.elementos.elementAt(i).matrizKGlobal[m][k]) + "    ");
                            }
                            sb.append("\r\n");
                        }


                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                        sb.append("\r\n");


                    }
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append("\r\n");


                    //Marco
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.datos_marco));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.cantidad_nodos)     +": "+ String.valueOf(marcoActual.cantidadNodos) + "    ");
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.cantidad_apoyos)    +": "+ String.valueOf(marcoActual.cantidadApoyos) + "    ");
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.cantidad_elementos) +": "+ String.valueOf(marcoActual.cantidadElementos));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.matriz_k_marco));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    for(int j = 0; j < marcoActual.cR; j++) {
                        if (j == 0){
                            sb.append("      " + String.valueOf(j));
                        } else {
                            sb.append("             " + String.valueOf(j));
                        }
                    }
                    sb.append("\r\n");
                    String ajMat;
                    for (int j = 0; j < marcoActual.cR; j++){
                        for (int k = 0; k < marcoActual.cR; k++){
                            if (marcoActual.matrizKMarco[j][k] < 0){
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }
                            sb.append(ajMat + String.format("%6.3e",marcoActual.matrizKMarco[j][k]) + "    ");
                        }
                        sb.append("\r\n");
                    }
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");


                    //Datos de Cargas
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.datos_cargas));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.fem) +" - "+ marcoActual.unidades[3].toString());
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    for (int i = 0; i < (marcoActual.elementos.size()); i++) {
                        sb.append(getResources().getText(R.string.ingreso_id_elemento) + ": " + marcoActual.elementos.elementAt(i).getId());
                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.angulo) + ": " + String.format("%6.3e",marcoActual.elementos.elementAt(i).anguloInclinacion));
                        sb.append("\r\n");
                        for (int j = 0; j < (marcoActual.elementos.elementAt(i).cargas.size()); j++) {
                            sb.append("RbNx: " + marcoActual.unidades[3] + String.format("%6.3e",(marcoActual.elementos.elementAt(i).cargas.elementAt(j).rbnx/1000)) + "    ");
                            sb.append("RbNy: " + marcoActual.unidades[3] + String.format("%6.3e",(marcoActual.elementos.elementAt(i).cargas.elementAt(j).rbny/1000)) + "    ");
                            sb.append("RbNm: " + marcoActual.unidades[5] + String.format("%6.3e",(marcoActual.elementos.elementAt(i).cargas.elementAt(j).rbnm/1000)) + "    ");
                            sb.append("\r\n");
                            sb.append("RbFx: " + marcoActual.unidades[3] + String.format("%6.3e",(marcoActual.elementos.elementAt(i).cargas.elementAt(j).rbfx/1000)) + "    ");
                            sb.append("RbFy: " + marcoActual.unidades[3] + String.format("%6.3e",(marcoActual.elementos.elementAt(i).cargas.elementAt(j).rbfy/1000)) + "    ");
                            sb.append("RbFm: " + marcoActual.unidades[5] + String.format("%6.3e",(marcoActual.elementos.elementAt(i).cargas.elementAt(j).rbfm/1000)) + "    ");

                            sb.append("\r\n");
                        }
                        sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                        sb.append("\r\n");
                    }


                    //Vector de cargas, Known  Nodal Loads
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.vector_cargas)  +": ");
                    sb.append("\r\n");
                    sb.append("\r\n");
                    int cantidadReaccionesNodos = marcoActual.nodos.size() * 3;
                    for (int k = 0; k < marcoActual.cRC; k++) {
                        if (marcoActual.Qk[k][0] < 0) {
                            ajMat = "";
                        } else {
                            ajMat = " ";
                        }
                        if (k < (marcoActual.cantidadNodos *3)){
                            if ((k+1) % 3 == 0){
                                sb.append( "|   " +ajMat + String.format("%6.3e",(marcoActual.Qk[k][0]/1000)) + "    |  " + marcoActual.unidades[5]+ " " + String.valueOf(k));  //Momento en Kn-m
                            } else {
                                sb.append( "|   " +ajMat + String.format("%6.3e",(marcoActual.Qk[k][0]/1000)) + "    |  " + marcoActual.unidades[3]+ " " + String.valueOf(k));  //Valor en n se pasa a Kn para su visualizacion
                            }
                            sb.append("\r\n");
                        } else if (k >= (marcoActual.cantidadNodos *3)) {
                            sb.append( "|   " +ajMat + String.format("%6.3e",(marcoActual.Qk[k][0]/1000)) + "    |  " + marcoActual.unidades[5]+ " " + String.valueOf(k));  //Momento en Kn-m
                            sb.append("\r\n");
                        }
                    }
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");


                    //Vector de desplazamientos conocidos
                    sb.append(getResources().getText(R.string.vector_des_con) +": ");
                    sb.append("\r\n");
                    sb.append("\r\n");


                    for (int k = marcoActual.cRC; k < (marcoActual.cR); k++){
                        ajMat = " ";

                        if (k < (marcoActual.cRC + (marcoActual.apoyosEmpotrados.size() *3))) {
                            if ((k + 1) % 3 == 0) {
                                sb.append("|   " + ajMat + "0" + "    |  " + marcoActual.unidades[8] + " " + String.valueOf(k));  //Momento en rad
                            } else {
                                if (marcoActual.sistemaInternacional){
                                    sb.append("|   " + ajMat + "0" + "    |  " + marcoActual.unidades[2] + " " + String.valueOf(k));  //Valor en m
                                }else {
                                    sb.append("|   " + ajMat + "0" + "    |  " + marcoActual.unidades[7] + " " + String.valueOf(k));  //Valor en in
                                }

                            }
                            sb.append("\r\n");

                        }else if (k >= (marcoActual.cRC + marcoActual.apoyosEmpotrados.size() *3)) {
                            if (marcoActual.sistemaInternacional){
                                sb.append("|   " + ajMat + "0" + "    |  " + marcoActual.unidades[2] + " " + String.valueOf(k));  //Valor en m
                            }else {
                                sb.append("|   " + ajMat + "0" + "    |  " + marcoActual.unidades[7] + " " + String.valueOf(k));  //Valor en in
                            }
                            sb.append("\r\n");
                        }


                    }
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    //RESULTADOS
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.datos_resultados));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");

                    //desplazamientos en nodos
                    sb.append(getResources().getText(R.string.q_kd));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.des_nodos));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    for (int k = 0; k < marcoActual.cRC; k++) {

                        if (marcoActual.Du[k][0] < 0) {
                            ajMat = "";
                        } else {
                            ajMat = " ";
                        }
                        if (k < (marcoActual.cantidadNodos * 3)) {
                            if ((k + 1) % 3 == 0) {
                                sb.append("|   " + ajMat + String.format("%6.4e", marcoActual.Du[k][0]) + "    |  " + marcoActual.unidades[8]+ " " + String.valueOf(k)); //rad
                            } else {
                                if (marcoActual.sistemaInternacional){
                                    sb.append("|   " + ajMat + String.format("%6.4e", marcoActual.Du[k][0]) + "    |  " + marcoActual.unidades[2] + " " + String.valueOf(k));
                                }else {
                                    sb.append("|   " + ajMat + String.format("%6.4e", marcoActual.Du[k][0]) + "    |  " + marcoActual.unidades[7] + " " + String.valueOf(k));
                                }

                            }
                            sb.append("\r\n");
                        }else if (k >= (marcoActual.cantidadNodos *3)) {
                            sb.append( "|   " +ajMat +  String.format("%6.4e", marcoActual.Du[k][0]) + "    |  " + marcoActual.unidades[8]+ " " + String.valueOf(k));  //rad
                            sb.append("\r\n");
                        }
                    }
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");


                    //Superponiendo FEM a Qu
                    int contiNodos = 0;
                    sb.append(getResources().getText(R.string.super_q_fem));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.qu_a));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.reac_apoyos)  + " + " +getResources().getText(R.string.fem ) );
                    sb.append("\r\n");

                    int k = 0;

                    if (marcoActual.apoyosNoEmpotrados.size() > 0){
                        for (int i = 0; i < marcoActual.apoyosNoEmpotrados.size(); i++){

                            marcoActual.apoyosNoEmpotrados.elementAt(i).rt1 = marcoActual.Qu[k][0] + marcoActual.apoyosNoEmpotrados.elementAt(i).rb1;
                            if ( marcoActual.apoyosNoEmpotrados.elementAt(i).rt1 < 0){
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }
                            sb.append( "|   " +ajMat + String.format("%6.4e",(marcoActual.Qu[k][0]/1000)) + " + "+ String.format("%6.4e",(marcoActual.apoyosNoEmpotrados.elementAt(i).rb1/1000))+" = "+ String.format("%6.4e",(marcoActual.apoyosNoEmpotrados.elementAt(i).rt1/1000)) + "    |  "+ marcoActual.unidades[3] + " "+ String.valueOf(k+marcoActual.cRC));
                            k += 1;
                            sb.append("\r\n");

                            marcoActual.apoyosNoEmpotrados.elementAt(i).rt2 = marcoActual.Qu[k][0] + marcoActual.apoyosNoEmpotrados.elementAt(i).rb2;
                            if ( marcoActual.apoyosNoEmpotrados.elementAt(i).rt2 < 0){
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }
                            sb.append( "|   " +ajMat + String.format("%6.4e",marcoActual.Qu[k][0]/1000 ) + " + "+ String.format("%6.4e",marcoActual.apoyosNoEmpotrados.elementAt(i).rb2/1000)+" = "+ String.format("%6.4e",marcoActual.apoyosNoEmpotrados.elementAt(i).rt2/1000) + "    |  "+ marcoActual.unidades[3] + " " + String.valueOf(k+marcoActual.cRC));
                            k += 1;
                            sb.append("\r\n");
                            marcoActual.apoyosNoEmpotrados.elementAt(i).rt3 = 0;

                        }
                    }

                    if (marcoActual.apoyosEmpotrados.size() > 0){
                        for (int i = 0; i < marcoActual.apoyosEmpotrados.size(); i++){

                            marcoActual.apoyosEmpotrados.elementAt(i).rt1 = marcoActual.Qu[k][0] + marcoActual.apoyosEmpotrados.elementAt(i).rb1;
                            if ( marcoActual.apoyosEmpotrados.elementAt(i).rt1 < 0){
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }
                            sb.append( "|   " +ajMat + String.format("%6.4e",marcoActual.Qu[k][0]/1000 ) + " + "+ String.format("%6.4e",marcoActual.apoyosEmpotrados.elementAt(i).rb1/1000)+" = "+ String.format("%6.4e",marcoActual.apoyosEmpotrados.elementAt(i).rt1/1000) + "    |  "+ marcoActual.unidades[3] + " " + String.valueOf(k+marcoActual.cRC));
                            k += 1;
                            sb.append("\r\n");

                            marcoActual.apoyosEmpotrados.elementAt(i).rt2 = marcoActual.Qu[k][0] + marcoActual.apoyosEmpotrados.elementAt(i).rb2;
                            if ( marcoActual.apoyosEmpotrados.elementAt(i).rt2 < 0){
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }
                            sb.append( "|   " +ajMat + String.format("%6.4e",marcoActual.Qu[k][0]/1000 ) + " + "+ String.format("%6.4e",marcoActual.apoyosEmpotrados.elementAt(i).rb2/1000)+" = "+ String.format("%6.4e",marcoActual.apoyosEmpotrados.elementAt(i).rt2/1000) + "    |  "+ marcoActual.unidades[3] + " " + String.valueOf(k+marcoActual.cRC));
                            k += 1;
                            sb.append("\r\n");
                            marcoActual.apoyosEmpotrados.elementAt(i).rt3 = marcoActual.Qu[k][0] + marcoActual.apoyosEmpotrados.elementAt(i).rb3;
                            if ( marcoActual.apoyosEmpotrados.elementAt(i).rt3 < 0){
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }
                            sb.append( "|   " +ajMat + String.format("%6.4e",marcoActual.Qu[k][0]/1000 ) + " + "+ String.format("%6.4e",marcoActual.apoyosEmpotrados.elementAt(i).rb3/1000)+" = "+ String.format("%6.4e",marcoActual.apoyosEmpotrados.elementAt(i).rt3/1000) + "    |  "+ marcoActual.unidades[5] + " " + String.valueOf(k+marcoActual.cRC));
                            k += 1;
                            sb.append("\r\n");
                        }
                    }


                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");


                    // Resultados en los Elementos
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.cargas_internas_elemento));
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.q_internas)  + " + " +getResources().getText(R.string.fem1 ) + " = " + getResources().getText(R.string.q_f ) );
                    sb.append("\r\n");
                    sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                    sb.append("\r\n");

                    for (int i = 0; i < (marcoActual.elementos.size()); i++) {

                        sb.append(getResources().getText(R.string.ingreso_id_elemento) + ": " + marcoActual.elementos.elementAt(i).getId());
                        sb.append("\r\n");
                        sb.append("\r\n");
                        String [][] idReac = new String[6][1];
                        idReac[0][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r1);
                        idReac[1][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r2);
                        idReac[2][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoN.r3);
                        idReac[3][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r1);
                        idReac[4][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r2);
                        idReac[5][0] = String.valueOf(marcoActual.elementos.elementAt(i).nodoF.r3);

                        for (int j = 0; j < 6; j++) {
                            if (marcoActual.elementos.elementAt(i).qInternas[j][0]<0) {
                                ajMat = "";
                            } else {
                                ajMat = " ";
                            }

                            if ((j+1) % 3 == 0) {
                                sb.append("|   " + ajMat + String.format("%6.4e", marcoActual.elementos.elementAt(i).qInternas[j][0]/1000) + " + " + String.format("%6.4e", marcoActual.elementos.elementAt(i).qFEM[j][0]/1000) + " = " + String.format("%6.4e", marcoActual.elementos.elementAt(i).qInternasF[j][0]/1000) + "    |  " + marcoActual.unidades[5] + " " + String.valueOf(idReac[j][0]));
                            } else {
                                sb.append("|   " + ajMat + String.format("%6.4e", marcoActual.elementos.elementAt(i).qInternas[j][0]/1000) + " + " + String.format("%6.4e", marcoActual.elementos.elementAt(i).qFEM[j][0]/1000) + " = " + String.format("%6.4e", marcoActual.elementos.elementAt(i).qInternasF[j][0]/1000) + "    |  " + marcoActual.unidades[3] + " " + String.valueOf(idReac[j][0]));
                            }
                            sb.append("\r\n");
                        }

                        sb.append("\r\n");
                        sb.append(getResources().getText(R.string.divisor_elemento_reporte));
                        sb.append("\r\n");


                    }
                    sb.append(getResources().getText(R.string.divisor_seccion_reporte));
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append("\r\n");

                } catch (InterruptedException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                running = false;
            }
            if (!running) return;
        }
    }




}







