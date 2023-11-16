package com.desarrollojlcp.structuralengineer2d;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import static androidx.core.content.PermissionChecker.PERMISSION_DENIED;


public class Ingreso extends AppCompatActivity implements View.OnClickListener {
    private VistaTrabajo vistaTrabajo;
    private Marco marcoActual;
    private static final int MI_PERMISO_WES = 1;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso2);
        FloatingActionButton botonCentrar = findViewById(R.id.boton_centrar);
        FloatingActionButton botonInfo = findViewById(R.id.boton_info);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permisoEscribir();
        }
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713



        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        seleccionDelUsuario();
        vistaTrabajo = (VistaTrabajo) findViewById(R.id.vista_trabajo);
        marcoActual = new Marco(0, 0, 0);
        marcoActual.primeraPantalla = true;
        preguntarUnidades();  // se soporta sistema internacional y sistema ingles
        vistaTrabajo.invalidate();

        FloatingActionButton botonAgregarSeccion = findViewById(R.id.boton_agregar_secTrans);
        botonAgregarSeccion.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonesFAB)));
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

        FloatingActionButton botonAgregarNodo = findViewById(R.id.boton_agregar_nodo);
        FloatingActionButton botonAgregarElemento = findViewById(R.id.boton_agregar_elemento);
        FloatingActionButton botonAgregarCarga = findViewById(R.id.boton_agregar_carga);
        FloatingActionButton botonAnalizar = findViewById(R.id.boton_analizar);

        botonAgregarNodo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));
        botonAgregarElemento.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));
        botonAgregarCarga.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));
        botonAnalizar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorHint)));

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
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                if (marcoActual.secciones.size() > 0) {

                } else {
                    String cadi = getResources().getText(R.string.minimos_crear_nodo).toString();
                    Toast.makeText(getApplicationContext(), cadi, Toast.LENGTH_SHORT).show();
                }

            }
        });

        botonAgregarElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                if ((marcoActual.secciones.size() > 0) && (marcoActual.getNodos().size() > 1)) {

                } else {
                    String cadi = getResources().getText(R.string.minimos_para_crear_elemento).toString();
                    Toast.makeText(getApplicationContext(), cadi, Toast.LENGTH_SHORT).show();
                }
            }
        });


        botonAgregarCarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                if (marcoActual.elementos.size() > 1) {

                } else {
                    String cadi = getResources().getText(R.string.minimos_para_crear_carga).toString();
                    Toast.makeText(getApplicationContext(), cadi, Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marcoActual.xCen = vistaTrabajo.xCen;
                marcoActual.yCen = vistaTrabajo.yCen;
                marcoActual.displayScale = vistaTrabajo.displayScale;
                boolean marcoCargado = false;
                if (marcoActual.elementos.size() > 0) {
                    for (int i = 0; i == marcoActual.elementos.size(); i++) {
                        if (marcoActual.elementos.elementAt(i).cargas.size() > 0) {
                            for (int j = 0; j == marcoActual.elementos.elementAt(i).cargas.size(); i++) {
                                marcoCargado = true;
                            }
                        }

                    }
                }
                if (marcoCargado) {

                } else {
                    String cadi = getResources().getText(R.string.minimos_para_crear_analizar).toString();
                    Toast.makeText(getApplicationContext(), cadi, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void seleccionDelUsuario(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(R.id.boton_agregar_secTrans));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "boton_agregar_secTrans");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
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

    public void permisoEscribir(){
        int permissionWES_Check = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionREAD_Check = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionWES_Check == PERMISSION_DENIED){
            if ((ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)) {


                // Should we show an explanation?

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MI_PERMISO_WES);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Toast.makeText(this,getResources().getText(R.string.explicar_wes).toString(),Toast.LENGTH_LONG).show();

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.



                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }

            }
        }

    }


    protected void preguntarUnidades(){
        CharSequence colors[] = new CharSequence[] {"mm - m - N - GPa", "in - ft - Lb - Ksi"};
        marcoActual.setSistemaUnidades(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Units");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                marcoActual.setSistemaUnidades(which);
            }
        });
        builder.show();
        marcoActual.xCen = 125;
        marcoActual.yCen = 900;
        marcoActual.displayScale = 4.0f;
        vistaTrabajo.actualizarVistaTrabajo(marcoActual);
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




}
