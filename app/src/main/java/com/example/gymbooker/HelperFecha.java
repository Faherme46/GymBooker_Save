package com.example.gymbooker;

import android.os.Build;

import com.example.gymbooker.Reserva.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HelperFecha {

    public static String timeToString(LocalTime hora){

        DateTimeFormatter formatter = null;
        String horaTexto=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            horaTexto = hora.format(formatter);

        }

        return horaTexto;
    }
    public LocalTime getHoraActual(){
        LocalTime horaActual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            horaActual = LocalTime.now();
        }
        return horaActual;
    }

    public LocalTime stringToTime(String hora){
        LocalTime horas=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            horas = LocalTime.parse(hora);
        }
        return horas;

    }


    public static boolean verificarExpiracion(Date fecha) {
        Calendar fechaActual = Calendar.getInstance();
        Calendar fechaComparacion = Calendar.getInstance();
        fechaComparacion.setTime(fecha);
        // Comparar las fechas
        if (fechaActual.after(fechaComparacion)) {
            // La fecha ya ha pasado
            return true;
        }
        // La fecha aún no ha pasado
        return false;
    }
    public LocalDate getFechaActual(){
        LocalDate fechaActual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaActual = LocalDate.now();
        }
        return fechaActual;
    }
    public ArrayList<Reserva> fechasPasadas(ArrayList<Reserva> listReservas){
        LocalDate fechaActual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            fechaActual = LocalDate.now();
        }
        ArrayList<Reserva> pasadas=new ArrayList<>();
        for (Reserva r : listReservas) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (toLocalDate(r.getFecha()).isBefore(fechaActual)) {
                    pasadas.add(r);
                }
            }
        }
        return pasadas;
    }
    public ArrayList<Reserva> fechasFuturas(ArrayList<Reserva> listReservas){
        LocalDate fechaActual = getFechaActual();
        ArrayList<Reserva> futuras=new ArrayList<>();
        for (Reserva r : listReservas) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (fechaActual.isBefore(toLocalDate(r.getFecha()))) {
                    futuras.add(r);
                }
            }
        }
        return futuras;
    }

    public LocalDate toLocalDate(String fecha){

        LocalDate localDate=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate= LocalDate.parse(fecha);
        }
        return  localDate;
    }

}

