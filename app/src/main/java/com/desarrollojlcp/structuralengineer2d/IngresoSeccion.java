package com.desarrollojlcp.structuralengineer2d;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class IngresoSeccion extends AppCompatActivity  {

    private TextView idSeccion,tituloIngresoArea, tituloIngresoInercia, tituloIngresoMuduloE;
    private EditText editArea, editInercia, editModulo;
    private Button botonGuardarSeguir, botonCancelar;
    int contadorSecciones = 0;
    private static Vector<Nodo> nodos = new Vector<Nodo>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_sec_trans);
        conectarIdentidades();
        botonGuardarSeguir.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        botonCancelar.setBackgroundColor(getResources().getColor(R.color.colorBlanco));
        final Marco marcoTrabajo = (Marco) getIntent().getExtras().getSerializable("marco_trabajo");

        tituloIngresoArea.setText(getResources().getString(R.string.area)+"     "+ marcoTrabajo.unidades[0]);
        tituloIngresoInercia.setText(getResources().getString(R.string.inercia )+"      "+ marcoTrabajo.unidades[1]);
        tituloIngresoMuduloE.setText(getResources().getString(R.string.modulo)+"        "+ marcoTrabajo.unidades[4]);

        contadorSecciones = marcoTrabajo.getContadorSecciones();
        contadorSecciones += 1;
        idSeccion.setText(String.valueOf(contadorSecciones));
        limpiarHints();
        botonGuardarSeguir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String ar;
                if (editArea.getText().toString().isEmpty()){
                    ar = "1";
                }else {
                    ar = editArea.getText().toString().replace(",",".");
                }
                String in;
                if (editInercia.getText().toString().isEmpty()){
                    in = "1";
                }else {
                    in = editInercia.getText().toString().replace(",",".");
                }
                String mo;
                if (editModulo.getText().toString().isEmpty()){
                    mo = "1";
                }else {
                    mo = editModulo.getText().toString().replace(",",".");
                }
                double area = Double.valueOf(ar);
                double inercia = Double.valueOf(in);
                double modulo = Double.valueOf(mo);
                SeccionTransversal seccionActual = new SeccionTransversal();

                if (marcoTrabajo.sistemaInternacional){
                    area = (area * 1000);  // pasando valor de notacion 1e3 a 10,000
                    inercia = (inercia * 1000000); //pasando valor de notacion 10e6 a 1,000,000
                    modulo = modulo;  //el valor se queda en GPa
                    seccionActual.setPropiedadesSistemaInternacional(contadorSecciones,area,inercia,modulo);
                } else if (marcoTrabajo.sistemaIngles){
                    area = area ;
                    inercia = inercia;
                    modulo = modulo *1000;  //pasando de notacion 10a la 3, a Ksi
                    seccionActual.setPropiedadesSistemaIngles(contadorSecciones,area,inercia,modulo);
                }

                marcoTrabajo.secciones.addElement(seccionActual);
                marcoTrabajo.setContadorSecciones(contadorSecciones);

                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("marco_trabajo", marcoTrabajo);
                finish();
                startActivity(intent);
                String cadi = getResources().getText(R.string.seccion_creada_correctamente).toString();
                Toast.makeText(getApplicationContext(),cadi,Toast.LENGTH_SHORT).show();
           }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marcoTrabajo.primeraPantalla){
                    marcoTrabajo.primeraPantalla = false;
                    Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                    intent.putExtra("marco_trabajo", marcoTrabajo);
                    finish();
                    startActivity(intent);
                }else {
                    finish();
                }


            }
        });
    }

    private void conectarIdentidades(){
        idSeccion = (TextView) findViewById(R.id.id_seccion);
        editArea = (EditText) findViewById(R.id.editArea);
        editInercia = (EditText) findViewById(R.id.editInercia);
        editModulo = (EditText) findViewById(R.id.editModulo);
        botonGuardarSeguir = (Button) findViewById(R.id.boton_guardar_seguir);
        tituloIngresoArea = (TextView) findViewById(R.id.titulo_ingreso_area);
        tituloIngresoInercia = (TextView) findViewById(R.id.titulo_ingreso_inercia);
        tituloIngresoMuduloE = (TextView) findViewById(R.id.titulo_ingreso_modulo_e);
        botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        editArea.setHintTextColor(getResources().getColor(R.color.colorHint));
        editInercia.setHintTextColor(getResources().getColor(R.color.colorHint));
        editModulo.setHintTextColor(getResources().getColor(R.color.colorHint));
    }

    private void limpiarHints(){
        editArea.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editArea.setHint("");
                else{
                    editArea.setHint("10.00");

                }
                    ;
            }
        });
        editInercia.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editInercia.setHint("");
                else{editInercia.setHint("300.00");
                   }

            }
        });
        editModulo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editModulo.setHint("");
                else{
                    editModulo.setHint("200.00");

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
