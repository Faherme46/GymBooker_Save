package com.example.gymbooker.Reserva;

import com.example.gymbooker.HelperFecha;

import java.io.Serializable;

public class Reserva implements Serializable {
    private String fecha, rutina,horaIngreso, horaSalida,duracion;
    private String cedula;

    private int   estado;

    //Estados=
    //0. Pendiente
    //1 Asistido
    //2 Cancelado



    public Reserva() {
        this.estado=0;
    }
    public Reserva(String fecha,String cedula, String rutina, String horaIngreso,String horaSalida) {
        this.fecha = fecha;
        this.cedula=cedula;
        this.rutina = rutina;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        HelperFecha helperFecha=new HelperFecha();
        this.estado=0;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRutina() {
        return rutina;
    }

    public void setRutina(String rutina) {
        this.rutina = rutina;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }
//TODO definir calculo de duracion
    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getCedula() {
        return cedula;
    }    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getEstado() {
        return estado;
    }    public void setEstado(int estado) {
        this.estado = estado;
    }
}