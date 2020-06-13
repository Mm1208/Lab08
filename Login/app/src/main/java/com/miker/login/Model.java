package com.miker.login;

import android.media.MediaPlayer;

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
    private int[] covers;

    public Model(ArrayList<User> users, User loggedUser,  int[] covers) {
        this.users = users;
        this.loggedUser = loggedUser;
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

    @Override
    public String toString() {
        return "Model{" +
                "users=" + users +
                ", loggedUser=" + loggedUser +
                '}';
    }

      public void initCanciones(){

        canciones = new ArrayList<Cancion>();

        canciones.add(new Cancion(1,"Believer"));
        canciones.add(new Cancion(2,"Locked Away"));
        canciones.add(new Cancion(3,"Phorograph"));
        canciones.add(new Cancion(4,"Youre Beautiful"));
        canciones.add(new Cancion(5,"La Bamba"));
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
