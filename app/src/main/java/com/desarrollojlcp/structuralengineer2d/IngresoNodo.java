package com.desarrollojlcp.structuralengineer2d;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Vector;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;


public class IngresoNodo extends AppCompatActivity  {

    private TextView idNodo,tituloIngresoXcoor, tituloIngresoYcoor;
    private EditText editCX, editCY;
    private RadioGroup radioGroupApoyo;
    private RadioButton apoyoSi, apoyoNo;
    private ImageButton botonApoyo2, botonApoyo3;
    private Button botonGuardarSeguir, botonCancelar,botonCrearOtroNodo;
    private boolean boton2Activo = false;
    private boolean boton3Activo = false;
    private int apoyo = 3;
    private boolean esApoyo = false;
    int contadorNodos = 0;
    private  Vector<Nodo> nodos = new Vector<Nodo>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_nodos);
        conectarIdentidades();


        final Marco marcoTrabajo = (Marco) getIntent().getExtras().getSerializable("marco_trabajo");
        tituloIngresoXcoor.setText( getResources().getText(R.string.ingreso_nodo_cx).toString() + " " + marcoTrabajo.unidades[2]);
        tituloIngresoYcoor.setText( getResources().getText(R.string.ingreso_nodo_cy).toString() + " " + marcoTrabajo.unidades[2]);

        contadorNodos = marcoTrabajo.getContadorNodos();
        contadorNodos += 1;



        idNodo.setText(String.valueOf(contadorNodos));
        apoyoNo.setChecked(true);
        boton2Activo = false;
        botonApoyo2.setEnabled(false);
        botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
        boton3Activo = false;
        botonApoyo3.setEnabled(false);
        botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
        botonGuardarSeguir.setEnabled(true);
        botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCancelar.setEnabled(true);
        botonCancelar.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCrearOtroNodo.setEnabled(true);
        botonCrearOtroNodo.setBackgroundColor(getResources().getColor(R.color.colorBlanco));

        limpiarHints();
        setRadioChange();

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                    intent.putExtra("marco_trabajo", marcoTrabajo);
                    finish();
                    startActivity(intent);

            }
        });

        botonCrearOtroNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marcoTrabajo.contadorNodos>8){
                    String cadi = getResources().getText(R.string.pague).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                }else{

                    String cooX;
                    if (editCX.getText().toString().isEmpty()){
                        cooX = "0";
                    }else {
                        cooX = editCX.getText().toString().replace(",",".");
                    }
                    String cooY;
                    if (editCY.getText().toString().isEmpty()){
                        cooY = "0";
                    }else {
                        cooY = editCY.getText().toString().replace(",",".");
                    }

                    double cX = Double.valueOf(cooX);
                    double cY = Double.valueOf(cooY);

                    Nodo nodoActual = new Nodo( contadorNodos, cX, cY, apoyo, esApoyo);

                    marcoTrabajo.agregarNodo(nodoActual);
                    marcoTrabajo.setContadorNodos(contadorNodos);


                    Intent intent = new Intent(getApplicationContext(), IngresoNodo.class);
                    intent.putExtra("marco_trabajo", marcoTrabajo);
                    finish();
                    startActivity(intent);
                    String cadi = getResources().getText(R.string.nodo_creado_correctamente).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                    if(marcoTrabajo.contadorNodos%4 == 0){

                    }
                }

            }
        });

        botonGuardarSeguir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (contadorNodos < 10) {
                    String cooX;
                    if (editCX.getText().toString().isEmpty()){
                        cooX = "0";
                    }else {
                        cooX = editCX.getText().toString().replace(",",".");
                    }
                    String cooY;
                    if (editCY.getText().toString().isEmpty()){
                        cooY = "0";
                    }else {
                        cooY = editCY.getText().toString().replace(",",".");
                    }

                    double cX = Double.valueOf(cooX);
                    double cY = Double.valueOf(cooY);

                    Nodo nodoActual = new Nodo( contadorNodos, cX, cY, apoyo, esApoyo);

                    marcoTrabajo.agregarNodo(nodoActual);
                    marcoTrabajo.setContadorNodos(contadorNodos);

                    Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                    intent.putExtra("marco_trabajo", marcoTrabajo);
                    finish();
                    startActivity(intent);
                    String cadi = getResources().getText(R.string.nodo_creado_correctamente).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();

                    if(marcoTrabajo.contadorNodos%4 == 0){

                    }
                } else {
                    String cadi = getResources().getText(R.string.pague).toString();
                    Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void conectarIdentidades(){
        idNodo = (TextView) findViewById(R.id.id_nodo);
        editCX = (EditText) findViewById(R.id.editXCoor);
        editCY = (EditText) findViewById(R.id.editYCoor);
        radioGroupApoyo = (RadioGroup) findViewById(R.id.radio_grupo_apoyo);
        apoyoSi = (RadioButton) findViewById(R.id.radio_apoyo_si);
        apoyoNo = (RadioButton) findViewById(R.id.radio_apoyo_no);
        botonApoyo2 = (ImageButton) findViewById(R.id.apoyo_2);
        botonApoyo3 = (ImageButton) findViewById(R.id.apoyo_3);
        botonGuardarSeguir = (Button) findViewById(R.id.boton_guardar_seguir);
        tituloIngresoXcoor = (TextView) findViewById(R.id.titulo_ingreso_xcoor);
        tituloIngresoYcoor = (TextView) findViewById(R.id.titulo_ingreso_ycoor);
        botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        botonCrearOtroNodo= (Button) findViewById(R.id.boton_agregar_otro_nodo);
        editCX.setHintTextColor(getResources().getColor(R.color.colorHint));
        editCY.setHintTextColor(getResources().getColor(R.color.colorHint));
    }

    private void setRadioChange(){
        radioGroupApoyo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (apoyoSi.isChecked()){
                    boton2Activo = true;
                    botonApoyo2.setEnabled(true);
                    botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    boton3Activo = true;
                    botonApoyo3.setEnabled(true);
                    botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    botonGuardarSeguir.setEnabled(false);
                    botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    botonCrearOtroNodo.setEnabled(false);
                    botonCrearOtroNodo.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    apoyo = 3;
                    esApoyo = true;
                } else if (apoyoNo.isChecked()){
                    boton2Activo = false;
                    botonApoyo2.setEnabled(false);
                    botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    boton3Activo = false;
                    botonApoyo3.setEnabled(false);
                    botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    botonGuardarSeguir.setEnabled(true);
                    botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    botonCrearOtroNodo.setEnabled(true);
                    botonCrearOtroNodo.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    apoyo = 3;
                    esApoyo =  false;
                }
                setClickListeners();
            }
        });
    }

    private void setClickListeners (){
        botonApoyo2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(boton2Activo){
                    boton2Activo = false;
                    botonApoyo2.setEnabled(false);
                    botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    boton3Activo = true;
                    botonApoyo3.setEnabled(true);
                    botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    botonGuardarSeguir.setEnabled(true);
                    botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    botonCrearOtroNodo.setEnabled(true);
                    botonCrearOtroNodo.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    apoyo = 2;
                } else {
                    boton2Activo = true;
                    botonApoyo2.setEnabled(true);
                    botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    boton3Activo = false;
                    botonApoyo3.setEnabled(false);
                    botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    apoyo = 3;
                }
            }
        });

        botonApoyo3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(boton3Activo){
                    boton3Activo = false;
                    botonApoyo3.setEnabled(false);
                    botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    boton2Activo = true;
                    botonApoyo2.setEnabled(true);
                    botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    botonGuardarSeguir.setEnabled(true);
                    botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    botonCrearOtroNodo.setEnabled(true);
                    botonCrearOtroNodo.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    apoyo = 3;
                } else {
                    boton2Activo = false;
                    botonApoyo2.setEnabled(false);
                    botonApoyo2.setBackgroundColor(getResources().getColor(R.color.colorBotonInactivo));
                    boton3Activo = true;
                    botonApoyo3.setEnabled(true);
                    botonApoyo3.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
                    apoyo = 2;
                }
            }
        });
    }

    private void limpiarHints(){
        editCX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editCX.setHint("");
                else
                    editCX.setHint("4.00");
            }
        });
        editCY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editCY.setHint("");
                else
                    editCY.setHint("4.00");
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
