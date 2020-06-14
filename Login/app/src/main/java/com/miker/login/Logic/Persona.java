package com.miker.login.Logic;

import java.io.Serializable;

public class Persona implements Serializable {
    private int id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private SEXO sexo;
    private String foto;

    public Persona(int id, String nombre, String apellido1, String apellido2, SEXO sexo, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.sexo = sexo;
        this.foto = foto;
    }

    public Persona(){
        this(-1, null, null, null, null, null);
    }

    public Persona(int id){
        this();
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

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public SEXO getSexo() {
        return sexo;
    }

    public void setSexo(SEXO sexo) {
        this.sexo = sexo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombreCompleto(){
        return nombre + " " + apellido1 + " " + apellido2;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", sexo=" + sexo +
                ", foto='" + foto + '\'' +
                '}';
    }
}
