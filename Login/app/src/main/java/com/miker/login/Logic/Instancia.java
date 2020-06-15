package com.miker.login.Logic;

import org.json.JSONObject;

import java.io.Serializable;

public abstract class Instancia implements Serializable{
    private static final long serialVersionUID = 1111111111L;

    public abstract JSONObject getJSON() throws Exception;
}
