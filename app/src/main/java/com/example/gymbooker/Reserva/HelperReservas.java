package com.example.gymbooker.Reserva;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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



    public void postReserva(Reserva r){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("fecha",r.getFecha());
        reserva.put("rutina",r.getRutina());
        reserva.put("horaIngreso",r.getHoraIngreso());
        reserva.put("horaSalida",r.getHoraSalida());


        db.collection("reserva")
                .add(reserva)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }
}
