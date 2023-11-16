package com.desarrollojlcp.structuralengineer2d;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    String[] nodosN;
    String resultado;




    public CustomSpinnerAdapter(Activity activity, String[] nodos ){
        this.nodosN = nodos;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CustomSpinnerAdapter( String[] nodos ){
        this.nodosN = nodos;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return nodosN.length;
    }

    @Override
    public long getItemId(int i){
        return  0;
    }

    @Override
    public Object getItem(int i){
            return null;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(nodosN[position]);
        resultado = ((TextView) convertView).getText().toString();
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(activity, android.R.layout.simple_spinner_item, null);
        textView.setText(nodosN[position]);
        resultado = textView.getText().toString();
        return textView;
    }

    public String getResultado(){
        return resultado;
    }
}
