package com.desarrollojlcp.structuralengineer2d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;

public class IngresoElemento extends AppCompatActivity {

    private Button botonGuardarSeguir, botonCancelar, botonCrearOtroElemento;
    private Nodo nodoN, nodoF;
    private SeccionTransversal seccionT;
    private TextView textIdElemento;
    int contadorElementos;
    CustomSpinnerAdapter spinnerAdapterN;
    CustomSpinnerAdapter spinnerAdapterF;
    CustomSpinnerAdapter spinnerAdapterS;
    String resultadoSpinnerN, resultadoSpinnerF;
    Elemento elementoTemporal;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_elementos);

        final Marco marcoTrabajo = (Marco) getIntent().getExtras().getSerializable("marco_trabajo");
        final Spinner spinnerN = (Spinner) findViewById(R.id.spinner_nodo_n);
        final Spinner spinnerF = (Spinner) findViewById(R.id.spinner_nodo_f);
        final Spinner spinnerS = (Spinner) findViewById(R.id.spinner_seccion);

        textIdElemento = (TextView) findViewById(R.id.id_elemento);
        botonGuardarSeguir = (Button) findViewById(R.id.boton_guardar_seguir);
        botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        botonCancelar.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCrearOtroElemento = (Button) findViewById(R.id.boton_agregar_otro_elemento);
        botonCrearOtroElemento.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

        contadorElementos = marcoTrabajo.getContadorElementos();
        contadorElementos += 1;
        textIdElemento.setText(String.valueOf(contadorElementos));



        //Set spiners para elegir nodos que forman el elemento
        if (marcoTrabajo.getNodos().size() > 0){
            String[] nodosN = new String[marcoTrabajo.getNodos().size()];
            for (int i = 0; i < marcoTrabajo.getNodos().size(); i++){
                nodosN[i] = String.valueOf(marcoTrabajo.getNodos().elementAt(i).getId()) + "    ("+
                        String.valueOf(marcoTrabajo.getNodos().elementAt(i).getCoordenadaX()) +","+
                        String.valueOf(marcoTrabajo.getNodos().elementAt(i).getCoordenadaY()) +")";
            }

            spinnerAdapterN = new CustomSpinnerAdapter(this, nodosN);
            spinnerN.setAdapter(spinnerAdapterN);

            final Activity activity = this;
            spinnerN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String idNodoN = spinnerAdapterN.getResultado();
                    String[] partesIdNodoN = idNodoN.split(" ");
                    String[] nodosF = new String[marcoTrabajo.getNodos().size()-1];
                    int contadorNodosF = 0;
                    for (int i = 0; i < marcoTrabajo.getNodos().size(); i++){
                        if ((marcoTrabajo.getNodos().elementAt(i).getId() == Integer.valueOf(partesIdNodoN[0]))){
                        }else{
                            nodosF[contadorNodosF] = String.valueOf(marcoTrabajo.getNodos().elementAt(i).getId()) + "    ("+
                                    String.valueOf(marcoTrabajo.getNodos().elementAt(i).getCoordenadaX()) +","+
                                    String.valueOf(marcoTrabajo.getNodos().elementAt(i).getCoordenadaY()) +")";
                            contadorNodosF += 1;
                        }
                    }
                    spinnerAdapterF = new CustomSpinnerAdapter(activity, nodosF);
                    spinnerF.setAdapter(spinnerAdapterF);
                    spinnerF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent1, View view, int position, long id) {
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        if (marcoTrabajo.secciones.size() > 0){
            String[] secciones = new String[marcoTrabajo.secciones.size()];
            for (int i = 0; i < marcoTrabajo.secciones.size(); i++){
                secciones[i] = String.valueOf(marcoTrabajo.secciones.elementAt(i).id)+" "+ getResources().getText(R.string.area_cm ).toString() +" "+ marcoTrabajo.unidades[0]+
                        String.valueOf(marcoTrabajo.secciones.elementAt(i).area);
            }
            spinnerAdapterS = new CustomSpinnerAdapter(this, secciones);
            spinnerS.setAdapter(spinnerAdapterS);
            spinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                finish();
                startActivity(intent);
            }
        });

        botonCrearOtroElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textSpinnerN = spinnerAdapterN.getResultado();
                String[] partesTSN = textSpinnerN.split(" ");
                String textSpinnerF = spinnerAdapterF.getResultado();
                String[] partesTSF = textSpinnerF.split(" ");
                String textSpinnerS = spinnerAdapterS.getResultado();
                String[] partesTSS = textSpinnerS.split(" ");

                int idNodoN = Integer.valueOf(partesTSN[0]);
                int idNodoF = Integer.valueOf(partesTSF[0]);
                int idSeccion = Integer.valueOf(partesTSS[0]);
                for (int i = 0; i < marcoTrabajo.getNodos().size(); i++){
                    if (marcoTrabajo.getNodos().elementAt(i).getId() == idNodoN){
                        nodoN = marcoTrabajo.getNodos().elementAt(i);
                    }
                    if (marcoTrabajo.getNodos().elementAt(i).getId() == idNodoF){
                        nodoF = marcoTrabajo.getNodos().elementAt(i);
                    }
                }
                for (int i = 0; i < marcoTrabajo.secciones.size(); i++){
                    if (marcoTrabajo.secciones.elementAt(i).id == idSeccion){
                        seccionT = marcoTrabajo.secciones.elementAt(i);
                    }
                }

                int idElem = contadorElementos;

                elementoTemporal = new Elemento(idElem,seccionT,nodoN,nodoF, marcoTrabajo.sistemaInternacional);

                marcoTrabajo.agregarElemento(elementoTemporal);
                marcoTrabajo.setContadorElementos(contadorElementos);
                Intent intent = new Intent(getApplicationContext(), IngresoElemento.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                finish();
                startActivity(intent);
                String cadi = getResources().getText(R.string.elemento_creado_correctamente).toString();
                Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                if(marcoTrabajo.contadorElementos%4 == 0){

                }
            }
        });


        botonGuardarSeguir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String textSpinnerN = spinnerAdapterN.getResultado();
                String[] partesTSN = textSpinnerN.split(" ");
                String textSpinnerF = spinnerAdapterF.getResultado();
                String[] partesTSF = textSpinnerF.split(" ");
                String textSpinnerS = spinnerAdapterS.getResultado();
                String[] partesTSS = textSpinnerS.split(" ");

                int idNodoN = Integer.valueOf(partesTSN[0]);
                int idNodoF = Integer.valueOf(partesTSF[0]);
                int idSeccion = Integer.valueOf(partesTSS[0]);
                for (int i = 0; i < marcoTrabajo.getNodos().size(); i++){
                    if (marcoTrabajo.getNodos().elementAt(i).getId() == idNodoN){
                        nodoN = marcoTrabajo.getNodos().elementAt(i);
                    }
                    if (marcoTrabajo.getNodos().elementAt(i).getId() == idNodoF){
                        nodoF = marcoTrabajo.getNodos().elementAt(i);
                    }
                }
                for (int i = 0; i < marcoTrabajo.secciones.size(); i++){
                    if (marcoTrabajo.secciones.elementAt(i).id == idSeccion){
                        seccionT = marcoTrabajo.secciones.elementAt(i);
                    }
                }

                int idElem = contadorElementos;

                elementoTemporal = new Elemento(idElem,seccionT,nodoN,nodoF, marcoTrabajo.sistemaInternacional);

                marcoTrabajo.agregarElemento(elementoTemporal);
                marcoTrabajo.setContadorElementos(contadorElementos);
                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                finish();
                startActivity(intent);
                String cadi = getResources().getText(R.string.elemento_creado_correctamente).toString();
                Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                if(marcoTrabajo.contadorElementos%4 == 0){

                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        botonCancelar.performClick();
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

}

