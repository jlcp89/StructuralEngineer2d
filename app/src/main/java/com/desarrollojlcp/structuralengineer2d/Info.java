package com.desarrollojlcp.structuralengineer2d;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class Info extends AppCompatActivity  {

    private TextView idSeccion,tituloIngresoArea, tituloIngresoInercia, tituloIngresoMuduloE;
    private EditText editArea, editInercia, editModulo;
    private Button botonGuardarSeguir, botonCancelar;
    int contadorSecciones = 0;
    private static Vector<Nodo> nodos = new Vector<Nodo>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        final Marco marcoTrabajo = (Marco) getIntent().getExtras().getSerializable("marco_trabajo");



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

        Button read = (Button) findViewById(R.id.boton_manual_usuario);
        Button youtube = (Button) findViewById(R.id.boton_manual_usuarioy);

        // Press the button and Call Method => [ ReadPDF ]
        read.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                abirManual();
            }
        });

        youtube.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                abirVideo();
            }
        });
    }

    public void abirManual() {
        String url = "http://www.d3sarrollo.com/manual-structural-engineer-2d-1-0/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void abirVideo() {
        String url = "http://youtu.be/3Aq7NpeFLPk";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
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
