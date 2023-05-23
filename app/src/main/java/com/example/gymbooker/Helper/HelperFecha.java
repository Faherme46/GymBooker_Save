package com.example.gymbooker.Helper;

import android.app.DatePickerDialog;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.gymbooker.Class.Reserva;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HelperFecha {


    public String entreHoras(String h1, String h2){
        LocalTime hora1=stringToTime(h1);
        LocalTime hora2=stringToTime(h2);
        Duration duration= null;
        String formattedDuration=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             duration= Duration.between(hora1,hora2);
            // Obtener las horas y minutos de la duración
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            // Formatear la duración en formato "hh:00"
            formattedDuration = String.format("%02d:%02d", hours,minutes);
        }

        return formattedDuration;
    }

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
                if (fechaActual.isBefore(toLocalDate(r.getFecha()))||fechaActual.isEqual(toLocalDate(r.getFecha()))) {
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

    public String formatoFecha(String fecha){
        LocalDate local= toLocalDate(fecha);

        return local.toString();

    }


    public void mostrarSelectorFecha(TextView et) {
        final Calendar calendar = Calendar.getInstance();
        int añoActual = calendar.get(Calendar.YEAR);
        int mesActual = calendar.get(Calendar.MONTH);
        int diaActual = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(et.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int año, int mes, int dia) {
                String fechaSeleccionada=null;
                if(mes<10) {
                    if (dia<10) {
                        fechaSeleccionada = año + "-0" + (mes + 1) + "-0" + dia;
                    }else{
                        fechaSeleccionada = año + "-0" + (mes + 1) + "-" + dia;
                    }

                }else{
                    if (dia<10) {
                        fechaSeleccionada = año + "-" + (mes + 1) + "-0" + dia;
                    }else{
                        fechaSeleccionada = año + "-" + (mes + 1) + "-" + dia;
                    }
                }
                et.setText(fechaSeleccionada);
            }

        }, añoActual, mesActual, diaActual);

        datePickerDialog.show();

    }

}

