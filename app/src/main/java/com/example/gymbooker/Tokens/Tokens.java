package com.example.gymbooker.Tokens;

import java.io.Serializable;

public class Tokens implements Serializable {
    private int isLimited;
    private String theToken,fVencimiento,fCreacion,idToken;
    private boolean used;

    public Tokens(){}

    public Tokens(int isLimited, String theToken, String fVencimiento, String fCreacion, String idToken, boolean used) {
        this.isLimited = isLimited;
        this.theToken = theToken;
        this.fVencimiento = fVencimiento;
        this.fCreacion = fCreacion;
        this.idToken = idToken;
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getIdToken() {
        return idToken;
    }public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getTheToken() {
        return theToken;
    }public void setTheToken(String theToken) {
        this.theToken = theToken;
    }

    public String getfVencimiento() {
        return fVencimiento;
    } public void setfVencimiento(String fVencimiento) {
        this.fVencimiento = fVencimiento;

    }public String getfCreacion() {
        return fCreacion;
    } public void setfCreacion(String fCreacion) {
        this.fCreacion = fCreacion;
    }

    public int isLimited(int i) {
        return isLimited;
    } public void setLimited(int limited) {
        isLimited = limited;
    }
}
