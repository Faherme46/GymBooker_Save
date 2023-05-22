package com.example.gymbooker.Tokens;

import java.io.Serializable;

public class Tokens implements Serializable {
    private double isLimited;
    private String theToken,fVencimiento,fCreacion;
    private boolean used;

    public Tokens(){}

    public Tokens( String theToken, String fVencimiento, String fCreacion) {
        this.isLimited = 0;
        this.theToken = theToken;
        this.fVencimiento = fVencimiento;
        this.fCreacion = fCreacion;
        this.used = false;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
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

    public double isLimited(int i) {
        return isLimited;
    } public void setLimited(double limited) {
        isLimited = limited;
    }
}
