package com.desarrollojlcp.structuralengineer2d;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

public class IngresoCarga extends AppCompatActivity  {

    private TextView textoF,textoA;
    CustomSpinnerAdapter spinnerAdapterElemento;

    private ImageButton boton1, boton2, boton3, boton4 ;
    private EditText editF, editA;
    private Button botonGuardarSeguir, botonCancelar, botonCrearOtraCarga;


    private int condiApoyo = 0;
    private int condiCarga = 0;
    int contadorCargas = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_cargas);
        conectarIdentidades();
        final Marco marcoTrabajo = (Marco) getIntent().getExtras().getSerializable("marco_trabajo");
        final Spinner spinnerE = (Spinner) findViewById(R.id.spinner_e);
        textoF.setText(getResources().getText(R.string.ingreso_f_1).toString()+ " " + marcoTrabajo.unidades[3]);
        textoA.setText(getResources().getText(R.string.ingreso_a).toString()+ " " + marcoTrabajo.unidades[2]);




        if (marcoTrabajo.elementos.size() > 0){
            //Variable para el spinner
            String[] elementosS = new String[marcoTrabajo.elementos.size()];
            //llenar el spinner con los datos de los elementos existentes
            for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                elementosS[i] = String.valueOf(marcoTrabajo.elementos.elementAt(i).getId());
            }
            //cargar spinner
            spinnerAdapterElemento = new CustomSpinnerAdapter(this, elementosS);
            //mostrar spinner
            spinnerE.setAdapter(spinnerAdapterElemento);

            spinnerE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        //posicion inicial de los botone de cargas
        boton1.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
        boton1.setEnabled(false);
        condiCarga = 1;
        boton2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        boton2.setEnabled(true);
        boton3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        boton3.setEnabled(true);
        boton4.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        boton4.setEnabled(true);

        //cuando un boton se selecciona, se desactiva, y los demas se activan para cambiar tipo de carga
        boton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boton1.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                boton1.setEnabled(false);
                condiCarga = 1;
                boton2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton2.setEnabled(true);
                boton3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton3.setEnabled(true);
                boton4.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton4.setEnabled(true);
                textoF.setText(getResources().getText(R.string.ingreso_f_1).toString()+ " " + marcoTrabajo.unidades[3]);
            }
        });

        boton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boton2.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                boton2.setEnabled(false);
                condiCarga = 2;
                boton1.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton1.setEnabled(true);
                boton3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton3.setEnabled(true);
                boton4.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton4.setEnabled(true);
                textoF.setText(getResources().getText(R.string.ingreso_f_23)+ " " + marcoTrabajo.unidades[3] + "/" + marcoTrabajo.unidades[2]);
            }
        });

        boton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boton3.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                boton3.setEnabled(false);
                condiCarga = 3;
                boton1.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton1.setEnabled(true);
                boton2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton2.setEnabled(true);
                boton4.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton4.setEnabled(true);
                textoF.setText(getResources().getText(R.string.ingreso_f_23)+ " " + marcoTrabajo.unidades[3] + "/" + marcoTrabajo.unidades[2]);
            }
        });

        boton4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                boton4.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                boton4.setEnabled(false);
                condiCarga = 4;
                boton1.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton1.setEnabled(true);
                boton2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton2.setEnabled(true);
                boton3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                boton3.setEnabled(true);
                textoF.setText(getResources().getText(R.string.ingreso_f_4)+ " " + marcoTrabajo.unidades[3] + "*" + marcoTrabajo.unidades[2]);
            }
        });



        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                finish();
                startActivity(intent);
            }
        });

        botonCrearOtraCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textSpinnerE = spinnerAdapterElemento.getResultado();
                int idElemento = Integer.valueOf(textSpinnerE);

                String F;
                if (editF.getText().toString().isEmpty()){
                    F = "0";
                }else {
                    F = editF.getText().toString().replace(",",".");
                }
                String A;
                if (editA.getText().toString().isEmpty()){
                    A = "0";
                }else {
                    A = editA.getText().toString().replace(",",".");
                }


                double f;
                double a;
                if (marcoTrabajo.sistemaInternacional){
                    f = Double.valueOf(F) * 1000;  // pasar de KN a N
                    a = Double.valueOf(A);
                    for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                        if (marcoTrabajo.elementos.elementAt(i).getId() == idElemento){
                            marcoTrabajo.elementos.elementAt(i).agregarCarga(condiCarga,f,a,marcoTrabajo.sistemaInternacional);
                            marcoTrabajo.yaCarga = true;
                        }
                    }
                } else {
                    f = Double.valueOf(F) * 1000;  // pasar de KLb-plg2 a Lb-plg2
                    a = Double.valueOf(A); //pasa en ft
                    for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                        if (marcoTrabajo.elementos.elementAt(i).getId() == idElemento){
                            marcoTrabajo.elementos.elementAt(i).agregarCarga(condiCarga,f,a,marcoTrabajo.sistemaInternacional);
                            marcoTrabajo.yaCarga = true;
                        }
                    }
                }

                marcoTrabajo.contadorCargas += 1;
                Intent intent = new Intent(getApplicationContext(), IngresoCarga.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                //guardarMarco(marcoTrabajo);
                finish();
                startActivity(intent);
                String cadi = getResources().getText(R.string.carga_creada_correctamente).toString();
                Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                if(marcoTrabajo.contadorCargas%3 == 0){

                }
            }
        });


        //ingresar magnitud de la carga y distancia de aplicacion
        botonGuardarSeguir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String textSpinnerE = spinnerAdapterElemento.getResultado();
                int idElemento = Integer.valueOf(textSpinnerE);

                String F;
                if (editF.getText().toString().isEmpty()){
                    F = "0";
                }else {
                    F = editF.getText().toString().replace(",",".");
                }
                String A;
                if (editA.getText().toString().isEmpty()){
                    A = "0";
                }else {
                    A = editA.getText().toString().replace(",",".");
                }


                double f;
                double a;
                if (marcoTrabajo.sistemaInternacional){
                    f = Double.valueOf(F) * 1000;  // pasar de KN a N
                    a = Double.valueOf(A);
                    for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                        if (marcoTrabajo.elementos.elementAt(i).getId() == idElemento){
                            if (a > marcoTrabajo.elementos.elementAt(i).L){
                                a = marcoTrabajo.elementos.elementAt(i).L;
                            }
                            marcoTrabajo.elementos.elementAt(i).agregarCarga(condiCarga,f,a,marcoTrabajo.sistemaInternacional);
                            marcoTrabajo.yaCarga = true;
                        }
                    }
                } else {
                    f = Double.valueOf(F) * 1000;  // pasar de KLb-plg2 a Lb-plg2
                    a = Double.valueOf(A); //pasa en ft
                    for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                        if (marcoTrabajo.elementos.elementAt(i).getId() == idElemento){
                            if (a > marcoTrabajo.elementos.elementAt(i).L){
                                a = marcoTrabajo.elementos.elementAt(i).L;
                            }
                            marcoTrabajo.elementos.elementAt(i).agregarCarga(condiCarga,f,a,marcoTrabajo.sistemaInternacional);
                            marcoTrabajo.yaCarga = true;
                        }
                    }
                }
                marcoTrabajo.contadorCargas +=1;
                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                //guardarMarco(marcoTrabajo);
                finish();
                startActivity(intent);
                String cadi = getResources().getText(R.string.carga_creada_correctamente).toString();
                Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                if(marcoTrabajo.contadorCargas%3 == 0){

                }
            }
        });

    }




    private void conectarIdentidades(){
        textoF = (TextView) findViewById(R.id.textof);
        textoA = (TextView) findViewById(R.id.textoa);
        boton1 = (ImageButton) findViewById(R.id.apoyo_2_1);
        boton2 = (ImageButton) findViewById(R.id.apoyo_2_2);
        boton3 = (ImageButton) findViewById(R.id.apoyo_2_3);
        boton4 = (ImageButton) findViewById(R.id.apoyo_2_4);
        editF = (EditText) findViewById(R.id.editF);
        editA = (EditText) findViewById(R.id.editA);
        editA.setHintTextColor(getResources().getColor(R.color.colorHint));
        editF.setHintTextColor(getResources().getColor(R.color.colorHint));
        botonGuardarSeguir = (Button)findViewById(R.id.boton_guardar_seguir);
        botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCancelar = (Button)findViewById(R.id.boton_cancelar);
        botonCancelar.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCrearOtraCarga = (Button)findViewById(R.id.boton_agregar_otra_carga);
        botonCrearOtraCarga.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

    }


    private void limpiarHints(){
        editF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editF.setHint("");
                else
                    editF.setHint("100.00");
            }
        });
        editA.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editA.setHint("");
                else
                    editA.setHint("2.00");
            }
        });

    }

}
