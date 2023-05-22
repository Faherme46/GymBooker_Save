package com.example.gymbooker.Reserva;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gymbooker.Tokens.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelperReservas {

    ArrayList<Reserva> listReserva = new ArrayList<>();
    public ArrayList<Reserva> getReservas() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("reserva").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot ReservaDocument = task.getResult();
                    if (ReservaDocument.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + ReservaDocument.getData());

                        Map<String, Object> originalMap = ReservaDocument.getData();
                        Map<String, Map<String, Object>> parsedMap = new HashMap<>();

                        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();

                            if (value instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> nestedMap = (Map<String, Object>) value;
                                parsedMap.put(key, nestedMap);
                            } else {
                            }
                        }

                        for (Map.Entry<String, Map<String, Object>> entry : parsedMap.entrySet()) {
                            String key = entry.getKey();
                            Reserva item = new Reserva();

                            item.setCedula((String) entry.getValue().get("cedula"));
                            item.setFecha((String) entry.getValue().get("fecha"));
                            item.setRutina((String) entry.getValue().get("cedula"));

                            listReserva.add(item);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }

        });



        return listReserva;
    }

    public ArrayList<Reserva> getReservasDefault(){
        listReserva=new ArrayList<>();
        Reserva r1= new Reserva("2023-05-18","1097608514","Abdomen","18:00","20:00");
        Reserva r2= new Reserva("2023-05-19","1097608514","Abdomen","18:00","20:00");
        Reserva r3= new Reserva("2023-05-20","1097608514","Abdomen","18:00","20:00");

        listReserva.add(r1);
        listReserva.add(r2);
        listReserva.add(r3);
        return listReserva;
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
