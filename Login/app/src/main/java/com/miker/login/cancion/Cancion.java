package com.miker.login.cancion;

import android.graphics.Bitmap;
import android.widget.TextView;

import org.json.JSONObject;

public class Cancion extends Instancia {

    private int id;
    private String nombre;
    private Bitmap bm;

    public Cancion(int id, String nombre,Bitmap bm) {
        this.id = id;
        this.nombre = nombre;
        this.bm = bm;
    }

    public Cancion() {
        this(-1, null,null);
    }

    public Cancion(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

       public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    @Override
    public String toString() {
        return "Cancion:\n\tId: " + id +  "\n\tNombre: " + nombre;
    }


    @Override
    public JSONObject getJSON() throws Exception {
        return null;
    }
}
