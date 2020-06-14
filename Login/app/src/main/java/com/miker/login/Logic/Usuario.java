package com.miker.login.Logic;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String user;
    private String password;
    private Persona person;

    public Usuario(int id, String user, String password, Persona person) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.person = person;
    }

    public Usuario() {
        this(-1, null, null, null);
    }

    public Usuario(int id) {
        this();
        this.id = id;
    }

    public Usuario(String user, String password) {
        this(-1, user, password, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Persona getPerson() {
        return person;
    }

    public void setPerson(Persona person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", person=" + person +
                '}';
    }
}
