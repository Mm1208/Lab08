package com.miker.login.Model;

import android.graphics.Bitmap;
import com.miker.login.Logic.Cancion;
import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable {
    private ArrayList<Cancion> canciones;
    private Cancion cancionSeleccionada;
    private Bitmap imagen = null;
    private int[] covers;

    public Model(int[] covers) {
        this.imagen = imagen;
        this.covers = covers;
    }

    public Model() {
        cancionSeleccionada = new Cancion();
        initCanciones();
    }

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }

    public int[] getCovers() {
        return covers;
    }

    public void setCovers(int[] covers) {
        this.covers = covers;
    }

    public Cancion getCancionSeleccionada() {
        return cancionSeleccionada;
    }

    public void setCancionSeleccionada(Cancion cancionSeleccionada) {
        this.cancionSeleccionada = cancionSeleccionada;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }


      public void initCanciones(){

        canciones = new ArrayList<Cancion>();

        canciones.add(new Cancion(1,"Believer",null));
        canciones.add(new Cancion(2,"Locked Away",null));
        canciones.add(new Cancion(3,"Phorograph",null));
        canciones.add(new Cancion(4,"Youre Beautiful",null));
        canciones.add(new Cancion(5,"La Bamba",null));
    }


}
