package com.miker.login;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.miker.login.cancion.Cancion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HsingPC on 20/4/2018.
 */

public class Model implements Serializable {
    private ArrayList<User> users;
    private User loggedUser;
    private ArrayList<Cancion> canciones;
    private Cancion cancionSeleccionada;
    private Bitmap imagen = null;
    private int[] covers;

    public Model(ArrayList<User> users, User loggedUser,  int[] covers) {
        this.users = users;
        this.loggedUser = loggedUser;
        this.imagen = imagen;
        this.covers = covers;
    }

    public Model() {
        this.loggedUser = new User();
        cancionSeleccionada = new Cancion();
        initCanciones();
        initUsers();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
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

    @Override
    public String toString() {
        return "Model{" +
                "users=" + users +
                ", loggedUser=" + loggedUser +
                '}';
    }

      public void initCanciones(){

        canciones = new ArrayList<Cancion>();

        canciones.add(new Cancion(1,"Believer",null));
        canciones.add(new Cancion(2,"Locked Away",null));
        canciones.add(new Cancion(3,"Phorograph",null));
        canciones.add(new Cancion(4,"Youre Beautiful",null));
        canciones.add(new Cancion(5,"La Bamba",null));
    }

    public void initUsers(){

        users = new ArrayList<User>();

        // User(String name, int privilege, ArrayList<Product> selectedProducts, String email, String password)
        User u = new User("Monse", 1,  "1", "1");
        users.add(u);


        u = new User("Luis", 2,"@luis", "");
        users.add(u);
    }


}
