package com.miker.login.cancion;

import org.json.JSONObject;

public class Cancion extends Instancia {

    private int id;
    private String nombre;

    public Cancion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Cancion() {
        this(-1, null);
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


    @Override
    public String toString() {
        return "Cancion:\n\tId: " + id +  "\n\tNombre: " + nombre;
    }


    @Override
    public JSONObject getJSON() throws Exception {
        return null;
    }
}
