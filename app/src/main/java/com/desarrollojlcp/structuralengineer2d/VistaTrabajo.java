package com.desarrollojlcp.structuralengineer2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("ClickableViewAccessibility")
public class VistaTrabajo extends View implements View.OnTouchListener {

    protected float displayScale = 4.0f;
    protected float xCen = 125;
    protected float yCen = 900;
    private float previousX = 0;
    private float previousY = 0;
    private Marco marcoTrabajo;
    private ScaleGestureDetector scaleDetector;
    protected Canvas canvasParaExportar;

    protected void actualizarVistaTrabajo(Marco marco){
        marcoTrabajo = marco;
        xCen = marco.xCen;
        yCen = marco.yCen;
        displayScale = marco.displayScale;
        invalidate();
    }

    public VistaTrabajo(Context context) {
        super(context);
    }

    public VistaTrabajo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scaleDetector = new ScaleGestureDetector(this.getContext(),new ScaleListener());
        setOnTouchListener(this);
    }

    public VistaTrabajo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VistaTrabajo(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void vistaTrabajo(){
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){
            float newDisplayScale = displayScale * detector.getScaleFactor();
            displayScale = newDisplayScale;
            invalidate();
            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        scaleDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                previousX = event.getX();
                previousY = event.getY();
                break;
            } case MotionEvent.ACTION_MOVE: {
                float deltaX = event.getX() - previousX;
                float deltaY = event.getY() - previousY;
                xCen += deltaX;
                yCen += deltaY;
                previousX = event.getX();
                previousY = event.getY();
                invalidate();
                break;
            } case MotionEvent.ACTION_UP: {
                break;
            } default: {
                break;
            }
        }
        return true;
    }

    public void onDraw(Canvas canvas){
        canvas.translate(xCen,yCen);
        canvas.scale(displayScale,displayScale);
        final Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorFondoVistaTrabajo));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        dibujarPlano(canvas);
        dibujarNodos(canvas);
        dibujarElementos(canvas);
        dibujarCargas(canvas);
        dibujarResultados(canvas);
        dibujarNodos(canvas);
        canvasParaExportar = canvas;
    }

    private void dibujarPlano(Canvas canvas){
        int x = 300;
        float cX = (float) (x * 20);
        float cY = (float) (x * -20);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int i = 1; i < x; i++){
            if((i%5)==0){
                if (displayScale<1.75f){
                    paint.setStrokeWidth((float)2);
                    paint.setColor(getResources().getColor(R.color.colorLineas5M));
                }else {
                    paint.setStrokeWidth((float)1);
                    paint.setColor(getResources().getColor(R.color.colorLineas5M));
                }
            }else {
                if (displayScale<1.85f){
                    paint.setStrokeWidth((float)0);
                    paint.setColor(getResources().getColor(R.color.colorFondoVistaTrabajo));
                }else {
                    paint.setStrokeWidth((float)0.5);
                    paint.setColor(getResources().getColor(R.color.colorLineas1M));
                }
            }
            float cX1 = (float) (i * 20);
            float cY1 = (float) (i * -20);
            canvas.drawLine(cX1, cY, cX1, (-1*cY), paint);
            canvas.drawLine(cX, cY1, (-1*cX),cY1, paint);
            canvas.drawLine(-cX1, cY, -cX1, (-1*cY), paint);
            canvas.drawLine(cX, -cY1, (-1*cX),-cY1, paint);
        }
        //paint.setColor(getResources().getColor(R.color.colorNegro));
        paint.setColor(getResources().getColor(R.color.colorEje));
        if (displayScale<1.75f){
            paint.setStrokeWidth((float)4);
        }else {
            paint.setStrokeWidth((float)1.5);
        }
        //Dibujar Plano Cartesiano
        canvas.drawLine(0, cY, 0, (-1*cY), paint);
        canvas.drawLine(cX, 0, (-1*cX),0, paint);

        paint.setTextSize(16f);
        paint.setStrokeWidth((float)3);
        paint.setColor(getResources().getColor(R.color.colorLineas1M));

        //Dibujar numeracion de plano
        for (int i = 1; i < x; i++){
            if((i%5)==0){
                String cadCoor = String.valueOf(i);
                canvas.drawText(cadCoor, -20, (i*(-20)) , paint);
                canvas.drawText("-"+cadCoor, -25,-1*(i*(-20)) , paint);
                canvas.drawText(cadCoor, (i*(20)), 14 , paint);
                canvas.drawText("-"+cadCoor, (i*(-20)), 14 , paint);
            }
        }
    }

    private void dibujarNodos(Canvas canvas){
        if (marcoTrabajo.nodos.size() > 0){
            for (int i = 0; i < marcoTrabajo.nodos.size(); i++){
                int id = marcoTrabajo.nodos.elementAt(i).getId();
                float cX = (float) (marcoTrabajo.nodos.elementAt(i).getCoordenadaX() * 20);
                float cY = (float) (marcoTrabajo.nodos.elementAt(i).getCoordenadaY() * -20);
                dibujarNodo(canvas,id,cX,cY,(float) marcoTrabajo.nodos.elementAt(i).getCoordenadaX(),(float) marcoTrabajo.nodos.elementAt(i).getCoordenadaY());
                if ((marcoTrabajo.nodos.elementAt(i).apoyo == 2)&&(marcoTrabajo.nodos.elementAt(i).esApoyo)){
                    dibujarApoyo2(canvas, cX, cY);
                } else if ((marcoTrabajo.nodos.elementAt(i).apoyo == 3)&&(marcoTrabajo.nodos.elementAt(i).esApoyo)){
                        dibujarApoyo3(canvas, cX, cY);
                }
            }
        }
    }

    private void dibujarNodo(Canvas canvas, int id, float cX, float cY, float coorX, float coorY){
        final Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        canvas.drawPoint(cX, cY, paint);
        canvas.drawCircle(cX, cY,3, paint);
        paint.setTextSize(4f);
        paint.setStrokeWidth(1);
        canvas.drawText(String.valueOf(id), cX - 10, cY +5, paint);
        paint.setTextSize(2f);
        paint.setStrokeWidth(0);
        String cadCoor = "(" + String.valueOf(coorX) + " , " + String.valueOf(coorY) + ")";
        canvas.drawText(cadCoor, cX - 13, cY+7 , paint);
    }

    private void dibujarApoyo2(Canvas canvas, float cX, float cY){
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        float x2 = cX - 5;
        float y2 = cY + 5;
        float x3 = cX + 5;
        float y3 = cY + 5;

        canvas.drawLine(cX,cY,x2,y2,paint);
        canvas.drawLine(cX,cY,x3,y3,paint);
        canvas.drawLine(x2,y2,x3,y3,paint);
        paint.setStrokeWidth(1);

        canvas.drawLine(cX +5,y2,cX +5 -3,y2+3,paint);
        canvas.drawLine(cX +0,y2,cX +0 -3,y2+3,paint);
        canvas.drawLine(cX -5,y2,cX -5 -3,y2+3,paint);
    }

    private void dibujarApoyo3(Canvas canvas, float cX, float cY){
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        float x2 = cX - 5;
        float y2 = cY + 0;
        float x3 = cX + 5;
        canvas.drawLine(x2,cY,x3,cY,paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(cX +5,y2,cX +5 -3,y2+3,paint);
        canvas.drawLine(cX +0,y2,cX +0 -3,y2+3,paint);
        canvas.drawLine(cX -5,y2,cX -5 -3,y2+3,paint);
    }

    private void dibujarElementos(Canvas canvas){
        if (marcoTrabajo.elementos.size() > 0){
            for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                int id = marcoTrabajo.elementos.elementAt(i).getId();
                Nodo nodoN = marcoTrabajo.elementos.elementAt(i).getNodoN();
                Nodo nodoF = marcoTrabajo.elementos.elementAt(i).getNodoF();
                float cXN = (float) (nodoN.getCoordenadaX() * 20);
                float cYN = (float) (nodoN.getCoordenadaY() * -20);
                float cXF = (float) (nodoF.getCoordenadaX() * 20);
                float cYF = (float) (nodoF.getCoordenadaY() * -20);
                if (cYN == 0) {
                    cYN = 0;
                }
                if (cYF == 0) {
                    cYF = 0;
                }
                float angulo = (float) marcoTrabajo.elementos.elementAt(i).getAnguloInclinacion();
                dibujarElemento(canvas,id,cXN,cYN,cXF,cYF,angulo);
            }
        }
    }

    private void dibujarElemento(Canvas canvas, int id, float cXN, float cYN, float cXF, float cYF, float anguloE){
        final Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);

        float cXMedio = (cXN + cXF) / 2;
        float cYMedio = (cYN + cYF) / 2;
        float deltaX = cXF - cXN;
        float deltaY = cYF - cYN;

        //Dibujar Elemento
        paint.setStrokeWidth(2);
        canvas.drawLine(cXN, cYN, cXF, cYF, paint);

        //calculo de coordenadas pata texto elemento
        float x0,y0;

        y0 = cYMedio;
        x0 = cXMedio;
        //Dibujar Texto
        paint.setTextSize(6f);
        paint.setStrokeWidth(1);
        if((anguloE > 0) && (anguloE < Math.toRadians(90))){
            if (deltaX > 0){
                x0 += 5;
                y0 += 2;
                canvas.drawText(String.valueOf(id),x0 , y0, paint);
                x0 += 0;
                y0 += 2;
            } else if (deltaX<0){
                x0 += 5;
                y0 += 2;
                canvas.drawText(String.valueOf(id),x0 , y0, paint);
                x0 += 0;
                y0 += 7;
            }
        } else if((anguloE < 0) && (anguloE > Math.toRadians(-90))){
            if (deltaY > 0){
                x0 += -7;
                y0 += 2;
                canvas.drawText(String.valueOf(id),x0 , y0, paint);
                x0 += 0;
                y0 += 4;
            } else if (deltaY < 0){
                x0 += -9;
                y0 += 4;
                canvas.drawText(String.valueOf(id),x0 , y0, paint);
                x0 += -3;
                y0 += 0;
            }

        }else {
            if (deltaY > 0) {
                x0 += 2;
                y0 += 0;
                canvas.drawText(String.valueOf(id), x0, y0, paint);
                x0 += 5;
                y0 += 5;
            } else if (deltaY < 0) {
                x0 += 2;
                y0 += 0;
                canvas.drawText(String.valueOf(id), x0, y0, paint);
                x0 += 5;
                y0 += -2;
            }else if (deltaY == 0){
                if (deltaX > 0){
                    x0 += 0;
                    y0 += 7;
                    canvas.drawText(String.valueOf(id),x0 , y0, paint);
                    x0 += 0;
                    y0 += 3;
                } else if (deltaX<0){
                    x0 += 0;
                    y0 += 7;
                    canvas.drawText(String.valueOf(id),x0 , y0, paint);
                    x0 += 0;
                    y0 += 3;
                }
            }
        }

        float x1,x2,x3,y1,y2,y3;
        if (deltaX > 0){
            //Calculo de coordenadas de flechaº*
            float angulo1 = (float) (anguloE + Math.toRadians(160));
            float angulo2 = (float) (anguloE + Math.toRadians(180));
            float angulo3 = (float) (anguloE + Math.toRadians(200));

            x1 = (float)(((0.2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((0.2 * Math.sin(angulo1)) * -20) + y0);
            x2 = (float)(((0.4 * Math.cos(angulo2)) *  20) + x0);
            y2 = (float)(((0.4 * Math.sin(angulo2)) * -20) + y0);
            x3 = (float)(((0.2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((0.2 * Math.sin(angulo3)) * -20) + y0);
            //Dibujar Flecha de direccion del elemento
            paint.setStrokeWidth(1);
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x2,y2,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);

        } else if(deltaX < 0){
            //Calculo de coordenadas de flechaº*
            float angulo1 = (float) (anguloE + Math.toRadians(20));
            float angulo2 = (float) (anguloE + Math.toRadians(0));
            float angulo3 = (float) (anguloE + Math.toRadians(-20));

            x2 = (float)(((0.4 * Math.cos(angulo2)) *  20) + x0);
            y2 = (float)(((0.4 * Math.sin(angulo2)) * -20) + y0);

            x1 = (float)(((0.2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((0.2 * Math.sin(angulo1)) * -20) + y0);
            x3 = (float)(((0.2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((0.2 * Math.sin(angulo3)) * -20) + y0);
            //Dibujar Flecha de direccion del elemento
            paint.setStrokeWidth(1);
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x2,y2,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);
        } else if(deltaX == 0){
            if (deltaY < 0){
                //Calculo de coordenadas de flechaº*
                float angulo1 = (float) (anguloE + Math.toRadians(160));
                float angulo2 = (float) (anguloE + Math.toRadians(180));
                float angulo3 = (float) (anguloE + Math.toRadians(200));

                x1 = (float)(((0.2 * Math.cos(angulo1)) *  20) + x0);
                y1 = (float)(((0.2 * Math.sin(angulo1)) * -20) + y0);
                x2 = (float)(((0.4 * Math.cos(angulo2)) *  20) + x0);
                y2 = (float)(((0.4 * Math.sin(angulo2)) * -20) + y0);
                x3 = (float)(((0.2 * Math.cos(angulo3)) *  20) + x0);
                y3 = (float)(((0.2 * Math.sin(angulo3)) * -20) + y0);
                //Dibujar Flecha de direccion del elemento
                paint.setStrokeWidth(1);
                canvas.drawLine(x0,y0,x1,y1,paint);
                canvas.drawLine(x0,y0,x2,y2,paint);
                canvas.drawLine(x0,y0,x3,y3,paint);
            }else if (deltaY > 0 ){
                //Calculo de coordenadas de flechaº*
                float angulo1 = (float) (anguloE + Math.toRadians(160));
                float angulo2 = (float) (anguloE + Math.toRadians(180));
                float angulo3 = (float) (anguloE + Math.toRadians(200));

                float xt,yt;
                x2 = (float)(((0.4 * Math.cos(angulo2)) *  20) + x0);
                y2 = (float)(((0.4 * Math.sin(angulo2)) * -20) + y0);

                x1 = (float)(((0.2 * Math.cos(angulo1)) *  20) + x0);
                y1 = (float)(((0.2 * Math.sin(angulo1)) * -20) + y0);
                x3 = (float)(((0.2 * Math.cos(angulo3)) *  20) + x0);
                y3 = (float)(((0.2 * Math.sin(angulo3)) * -20) + y0);
                //Dibujar Flecha de direccion del elemento
                paint.setStrokeWidth(1);
                canvas.drawLine(x0,y0,x1,y1,paint);
                canvas.drawLine(x0,y0,x2,y2,paint);
                canvas.drawLine(x0,y0,x3,y3,paint);
            }
        }
    }

    private void dibujarCargas(Canvas canvas){
        if (marcoTrabajo.elementos.size() > 0){
            for (int i = 0; i < marcoTrabajo.elementos.size(); i++){
                if (marcoTrabajo.elementos.elementAt(i).cargas.size() > 0){
                    for (int j = 0; j < marcoTrabajo.elementos.elementAt(i).cargas.size(); j++){
                        Elemento elementoT = marcoTrabajo.elementos.elementAt(i);
                        Carga cargaT = marcoTrabajo.elementos.elementAt(i).cargas.elementAt(j);
                        dibujarCarga(canvas,elementoT,cargaT);
                    }
                }
            }
        }
    }

    private void dibujarCarga(Canvas canvas, Elemento elemento, Carga carga){
        float angulo = (float) elemento.getAnguloInclinacion();
        Nodo nodoN = elemento.getNodoN();
        Nodo nodoF = elemento.getNodoF();

        float cXN = (float) (nodoN.getCoordenadaX() * 20);
        float cYN = (float) (nodoN.getCoordenadaY() * -20);
        float cXF = (float) (nodoF.getCoordenadaX() * 20);
        float cYF = (float) (nodoF.getCoordenadaY() * -20);
        float deltaX = cXF - cXN;
        float deltaY = cYF - cYN;
        float xA = 0;
        if (deltaX >= 0 ){
            xA = 1;
        } else {
            xA = -1;
        }
        float cXA = (float) ((nodoN.getCoordenadaX() + ((carga.getA()* xA) * Math.cos(angulo)))  *  20);
        float cYA = (float) ((nodoN.getCoordenadaY() + ((carga.getA()* xA) * Math.sin(angulo)))  * -20);


        if (cYN == 0) {
        cYN = 0;
        }
        if (cYF == 0) {
        cYF = 0;
        }
        if (cYA == 0) {
        cYA = 0;
        }

        final Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorCarga));
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(1);
        paint.setTextSize(3f);



        float x0,y0;
        float x1,x2,x3,y1,y2,y3;
        float tamañoFlecha;

        if (carga.getF() >= 0 ){
            tamañoFlecha = (float) (0.4);
        } else {
            tamañoFlecha = (float) (-0.4);
        }


        if (carga.getCasoCarga() == 1){
            x0 = cXA;
            y0 = cYA;

            //Calculo de coordenadas de flechaº*
            float angulo1 = (float) (angulo + Math.toRadians(70));
            float angulo2 = (float) (angulo + Math.toRadians(90));
            float angulo3 = (float) (angulo + Math.toRadians(110));



            x1 = (float)(((tamañoFlecha/2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((tamañoFlecha/2 * Math.sin(angulo1)) * -20) + y0);
            x2 = (float)(((tamañoFlecha * Math.cos(angulo2)) *  20) + x0);
            y2 = (float)(((tamañoFlecha * Math.sin(angulo2)) * -20) + y0);
            x3 = (float)(((tamañoFlecha/2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((tamañoFlecha/2 * Math.sin(angulo3)) * -20) + y0);

            //Dibujar Flecha de direccion del elemento
            paint.setStrokeWidth(1);
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x2,y2,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);
            paint.setTextSize(2f);
            paint.setStrokeWidth(0);

            canvas.drawText(String.valueOf(carga.getF()/1000) + " kN",x2-7 , y2+0, paint);

        } else if (carga.getCasoCarga() == 2){
            // Primera flecha
            x0 = cXN;
            y0 = cYN;

            //Calculo de coordenadas de flechaº*
            float angulo1 = (float) (angulo + Math.toRadians(70));
            float angulo2 = (float) (angulo + Math.toRadians(90));
            float angulo3 = (float) (angulo + Math.toRadians(110));

            float x1L,y1L,x2L,y2L;


            x1 = (float)(((tamañoFlecha/2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((tamañoFlecha/2 * Math.sin(angulo1)) * -20) + y0);
            x2 = (float)(((tamañoFlecha * Math.cos(angulo2)) *  20) + x0);
            y2 = (float)(((tamañoFlecha * Math.sin(angulo2)) * -20) + y0);
            x3 = (float)(((tamañoFlecha/2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((tamañoFlecha/2 * Math.sin(angulo3)) * -20) + y0);
            x1L = x2;
            y1L = y2;

            //Dibujar Flecha de direccion del elemento
            paint.setStrokeWidth(1);
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x2,y2,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);

            int cantidadFlechas = (int) (carga.getA() / 0.25);

            //Dibujar flechas de adentro

            for (int i = 0; i < cantidadFlechas; i++){
                float cXAt = (float) ((nodoN.getCoordenadaX() + (((i * 0.25)* xA) * Math.cos(angulo)))  *  20);
                float cYAt = (float) ((nodoN.getCoordenadaY() + (((i * 0.25)* xA) * Math.sin(angulo)))  * -20);

                // dibujar 4 flechcuantas flechas se van a dibuj
                x0 = cXAt;
                y0 = cYAt;

                x1 = (float)(((tamañoFlecha/2 * Math.cos(angulo1)) *  20) + x0);
                y1 = (float)(((tamañoFlecha/2 * Math.sin(angulo1)) * -20) + y0);
                x2 = (float)(((tamañoFlecha * Math.cos(angulo2)) *  20) + x0);
                y2 = (float)(((tamañoFlecha * Math.sin(angulo2)) * -20) + y0);
                x3 = (float)(((tamañoFlecha/2 * Math.cos(angulo3)) *  20) + x0);
                y3 = (float)(((tamañoFlecha/2 * Math.sin(angulo3)) * -20) + y0);

                //Dibujar Flecha de direccion del elemento
                canvas.drawLine(x0,y0,x1,y1,paint);
                canvas.drawLine(x0,y0,x2,y2,paint);
                canvas.drawLine(x0,y0,x3,y3,paint);

            }

            //Dibujar ultima flecha
            x0 = cXA;
            y0 = cYA;

            x1 = (float)(((tamañoFlecha/2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((tamañoFlecha/2 * Math.sin(angulo1)) * -20) + y0);
            x2 = (float)(((tamañoFlecha * Math.cos(angulo2)) *  20) + x0);
            y2 = (float)(((tamañoFlecha * Math.sin(angulo2)) * -20) + y0);
            x3 = (float)(((tamañoFlecha/2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((tamañoFlecha/2 * Math.sin(angulo3)) * -20) + y0);
            x2L = x2;
            y2L = y2;
            //Dibujar Flecha de direccion del elemento
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x2,y2,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);
            canvas.drawLine(x1L,y1L,x2L,y2L,paint);

            paint.setTextSize(2f);
            paint.setStrokeWidth(0);

            float xTemp, yTemp;
            if (deltaY>0){
                xTemp = +3;
                yTemp = 0;
            }else if (deltaY< 0){
                xTemp = -12;
                yTemp = 0;
            }else {
                xTemp = 0;
                yTemp = -2;
            }
            float xM = (x2L + x1L) / 2;
            float yM = (y2L + y1L) / 2;

            canvas.drawText(String.valueOf(carga.getF()/1000) + " kN/m",xM+xTemp , yM+yTemp, paint);

        } else if (carga.getCasoCarga() == 3){
            // Primera flecha
            x0 = cXA;
            y0 = cYA;
            paint.setStrokeWidth(1);
            //Calculo de angulos de coordenadas de flechaº*
            float angulo1 = (float) (angulo + Math.toRadians(70));
            float angulo2 = (float) (angulo + Math.toRadians(90));
            float angulo3 = (float) (angulo + Math.toRadians(110));

            float x1L,y1L,x2L,y2L;

            x1L = x0;
            y1L = y0;

            int cantidadFlechas = (int) (carga.getB() / 0.25);
            double anguloTriangulo = Math.atan(tamañoFlecha/carga.getB());
            float xParcial = 0;

            //Dibujar flechas de adentro

            for (int i = 1; i < cantidadFlechas; i++){
                float cXAt = (float) ((nodoN.getCoordenadaX() + (((carga.getA() + (i * 0.25))* xA) * Math.cos(angulo)))  *  20);
                float cYAt = (float) ((nodoN.getCoordenadaY() + (((carga.getA() + (i * 0.25))* xA) * Math.sin(angulo)))  * -20);

                // dibujar 4 flechcuantas flechas se van a dibuj
                x0 = cXAt;
                y0 = cYAt;

                float alturaFlecha = (float)((i * 0.25) * Math.tan(anguloTriangulo));

                x1 = (float)(((alturaFlecha/2 * Math.cos(angulo1)) *  20) + x0);
                y1 = (float)(((alturaFlecha/2 * Math.sin(angulo1)) * -20) + y0);
                x2 = (float)(((alturaFlecha * Math.cos(angulo2)) *  20) + x0);
                y2 = (float)(((alturaFlecha * Math.sin(angulo2)) * -20) + y0);
                x3 = (float)(((alturaFlecha/2 * Math.cos(angulo3)) *  20) + x0);
                y3 = (float)(((alturaFlecha/2 * Math.sin(angulo3)) * -20) + y0);

                //Dibujar Flecha de direccion del elemento
                canvas.drawLine(x0,y0,x1,y1,paint);
                canvas.drawLine(x0,y0,x2,y2,paint);
                canvas.drawLine(x0,y0,x3,y3,paint);

            }

            //Dibujar ultima flecha
            x0 = cXF;
            y0 = cYF;

            x1 = (float)(((tamañoFlecha/2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((tamañoFlecha/2 * Math.sin(angulo1)) * -20) + y0);
            x2 = (float)(((tamañoFlecha * Math.cos(angulo2)) *  20) + x0);
            y2 = (float)(((tamañoFlecha * Math.sin(angulo2)) * -20) + y0);
            x3 = (float)(((tamañoFlecha/2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((tamañoFlecha/2 * Math.sin(angulo3)) * -20) + y0);
            x2L = x2;
            y2L = y2;
            //Dibujar Flecha de direccion del elemento
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x2,y2,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);
            canvas.drawLine(x1L,y1L,x2L,y2L,paint);

            paint.setTextSize(3f);
            paint.setStrokeWidth(0);

            float xTemp, yTemp;
            if (deltaY>0){
                xTemp = +15;
                yTemp = 0;
            }else if (deltaY< 0){
                xTemp = -15;
                yTemp = 0;
            }else {
                xTemp = 0;
                yTemp = -2;
            }
            if (tamañoFlecha < 0) {
                if (deltaY > 0){
                    xTemp += -18;
                }else{
                    xTemp += +18;
                }
            }

            float xM = (x2L + x1L) / 2;
            float yM = (y2L + y1L) / 2;

            canvas.drawText(String.valueOf(carga.getF()/1000) + " kN/m",xM+xTemp , yM+yTemp, paint);

        } else if (carga.getCasoCarga() == 4){
            // Primera flecha
            x0 = cXA;
            y0 = cYA;
            paint.setStrokeWidth(1);
            float angulo1, angulo3;
            //Calculo de angulos de coordenadas de flechaº*
            if (tamañoFlecha >= 0){
                angulo1 = (float) (Math.toRadians(180));
                angulo3 = (float) (Math.toRadians(220));

            } else {
                angulo1 = (float) (Math.toRadians(320));
                angulo3 = (float) (Math.toRadians(360));
            }


            //Dibujar Flecha de direccion del elemento
            RectF rectF = new RectF(x0-5, y0 - 5, x0 +5, y0+5);
            canvas.drawArc (rectF, 90, 180, false, paint);
            float posicionFlecha;
            if (tamañoFlecha < 0){
                posicionFlecha = -5;
            }else {
                posicionFlecha = +5;
            }
            y0 += posicionFlecha;
            x1 = (float)(((tamañoFlecha/2 * Math.cos(angulo1)) *  20) + x0);
            y1 = (float)(((tamañoFlecha/2 * Math.sin(angulo1)) * -20) + y0);

            x3 = (float)(((tamañoFlecha/2 * Math.cos(angulo3)) *  20) + x0);
            y3 = (float)(((tamañoFlecha/2 * Math.sin(angulo3)) * -20) + y0);
            canvas.drawLine(x0,y0,x1,y1,paint);
            canvas.drawLine(x0,y0,x3,y3,paint);

            paint.setTextSize(3f);
            paint.setStrokeWidth(0);

            float xTemp, yTemp;
            if (deltaY>0){
                xTemp = +15;
                yTemp = 0;
            }else if (deltaY< 0){
                xTemp = -15;
                yTemp = 0;
            }else {
                xTemp = 0;
                yTemp = -4;
            }
            if (tamañoFlecha < 0) {
                if (deltaY > 0){
                    xTemp += -18;
                }else{
                    xTemp += 18;
                }
            }

            canvas.drawText(String.valueOf(carga.getF()/1000) + " kN*m",x0+xTemp+2 , y0+yTemp-2, paint);

        }
    }

    private void dibujarResultados(Canvas canvas){
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(3f);
        paint.setStrokeWidth(0);
        if (marcoTrabajo.marcoAnalizado){
            for (int i = 0; i < marcoTrabajo.nodos.size(); i++){
                float cX = (float) (marcoTrabajo.nodos.elementAt(i).getCoordenadaX() * 20);
                float cY = (float) (marcoTrabajo.nodos.elementAt(i).getCoordenadaY() * -20);
                String texto;
                if (marcoTrabajo.sistemaInternacional) {
                    paint.setColor(Color.RED);
                    if (marcoTrabajo.nodos.elementAt(i).apoyo == 2 || marcoTrabajo.nodos.elementAt(i).apoyo == 3){
                        texto = "Rx" + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).rt1/1000));
                        canvas.drawText(texto, cX - 13, cY+10 , paint);
                        texto = "Ry" + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).rt2/1000));
                        canvas.drawText(texto, cX - 13, cY+13 , paint);
                        texto = "Rz" + marcoTrabajo.unidades[5]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).rt3/1000));
                        canvas.drawText(texto, cX - 15, cY+16 , paint);
                    } else {
                        texto = "Rx" + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).ra1/1000));
                        canvas.drawText(texto, cX - 13, cY+10 , paint);
                        texto = "Ry" + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).ra2/1000));
                        canvas.drawText(texto, cX - 13, cY+13 , paint);
                        texto = "Rz" + marcoTrabajo.unidades[5]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).ra3/1000));
                        canvas.drawText(texto, cX - 15, cY+16 , paint);
                    }
                    paint.setColor(getResources().getColor(R.color.colorLineas5M));
                    texto = "Dx " + marcoTrabajo.unidades[2]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).des1));
                    canvas.drawText(texto, cX - 13, cY+19 , paint);
                    texto = "Dy " + marcoTrabajo.unidades[2]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).des2));
                    canvas.drawText(texto, cX - 13, cY+22 , paint);

                } else {
                    paint.setColor(Color.RED);
                    if (marcoTrabajo.nodos.elementAt(i).apoyo == 2 || marcoTrabajo.nodos.elementAt(i).apoyo == 3){
                        texto = "Rx " + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).rt1/1000));
                        canvas.drawText(texto, cX - 13, cY+10 , paint);
                        texto = "Ry " + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).rt2/1000));
                        canvas.drawText(texto, cX - 13, cY+13 , paint);
                        texto = "Rz " + marcoTrabajo.unidades[5]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).rt3/1000));
                        canvas.drawText(texto, cX - 15, cY+16 , paint);
                    } else {
                        texto = "Rx " + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).ra1/1000));
                        canvas.drawText(texto, cX - 13, cY+10 , paint);
                        texto = "Ry " + marcoTrabajo.unidades[3]+ "  = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).ra2/1000));
                        canvas.drawText(texto, cX - 13, cY+13 , paint);
                        texto = "Rz " + marcoTrabajo.unidades[5]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).ra3/1000));
                        canvas.drawText(texto, cX - 15, cY+16 , paint);

                    }
                    paint.setColor(getResources().getColor(R.color.colorLineas5M));
                    texto = "Dx " + marcoTrabajo.unidades[7]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).des1));
                    canvas.drawText(texto, cX - 13, cY+19 , paint);
                    texto = "Dy " + marcoTrabajo.unidades[7]+ " = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).des2));
                    canvas.drawText(texto, cX - 13, cY+22 , paint);
                }

                paint.setColor(getResources().getColor(R.color.colorLineas5M));
                texto = "Dz (rad) = " + String.format("%6.4e",(marcoTrabajo.nodos.elementAt(i).des3));
                canvas.drawText(texto, cX - 13, cY+25 , paint);
            }
        }
    }

    public void  guardarImagen(){
        final String fecha1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        String nomCarpeta = "Reports - StructuralAnalysis2d";
        String ano = fecha1.substring(17, 19);
        String mes = fecha1.substring(12, 14);
        String dia = fecha1.substring(9, 11);
        String hor = fecha1.substring(0, 2);
        String min = fecha1.substring(3, 5);
        String fechaNombreReporte = ano + mes + dia + hor + min;
        fechaNombreReporte.replace(" ", "");
        String rutaCarpetaActual = tarjetaSD + File.separator + nomCarpeta;
        String nombreArchivoJPEG = fechaNombreReporte + ".jpg";
        String rutaArchivoJPEG = rutaCarpetaActual + File.separator + nombreArchivoJPEG;
        File file;
        Uri uri = Uri.parse(rutaArchivoJPEG);
        file = new File(uri.getPath());
        file.getParentFile().mkdirs();

        int medidaX = (int) ((marcoTrabajo.getMaxXPos()*2 +50)*30);
        int medidaY = (int) ((marcoTrabajo.getMaxYPos()*2 +50)*30);
        this.xCen = 125;
        this.yCen = 1300;
        this.displayScale= 4.0f;

        Bitmap  bitmap = Bitmap.createBitmap(medidaX, medidaY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        try {
            saveBitmapToJpg(bitmap,file,1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.xCen = marcoTrabajo.xCen;
        this.yCen = marcoTrabajo.yCen;
        this.displayScale = marcoTrabajo.displayScale;
    }

    public static void saveBitmapToJpg(Bitmap bitmap, File file, int dpi) throws IOException {
        ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
        byte[] imageData = imageByteArray.toByteArray();

        setDpi(imageData, dpi);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(imageData);
        fileOutputStream.close();
    }

    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
    }

}
