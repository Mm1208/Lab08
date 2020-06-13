package com.miker.login;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HsingPC on 20/4/2018.
 */

public class User implements Serializable {

    private String name;
    private int privilege;
    private String email;
    private String password;

    public User(String name, int privilege,  String email, String password) {
        this.name = name;
        this.privilege = privilege;
        this.email = email;
        this.password = password;
    }

    public User() {
        this.name = "";
        this.privilege = 0;
        this.email = "@";
        this.password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", privilege=" + privilege +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
