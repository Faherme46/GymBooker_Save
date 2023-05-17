package com.example.gymbooker.Reserva;

import java.util.ArrayList;

public class HelperReservas {

    ArrayList<Reserva> listReservas = new ArrayList<>();
    public ArrayList<Reserva> getReserva() {

        Reserva res1 = new Reserva("2023-05-12","1097608514","Espalda","16:00","18:00");
        Reserva res2 = new Reserva("2023-05-13","1097608514","Abdomen","09:00","11:00");
        Reserva res3 = new Reserva("2023-05-17","1097608514","Abdomen","09:00","11:00");
        listReservas.add(res1);
        listReservas.add(res2);
        listReservas.add(res3);
        return listReservas;
    }



    public void guardarReserva(Reserva r){

    }
}
