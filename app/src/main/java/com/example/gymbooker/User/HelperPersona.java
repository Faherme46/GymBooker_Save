package com.example.gymbooker.User;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gymbooker.Tokens.Tokens;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelperPersona {
    private static final String API_Url =  "https://apex.oracle.com/pls/apex/gymbooker/RESTAPI_GYMBOOKER/";

    ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUser() {
        ArrayList<User> users = new ArrayList<>();
        User u1 = new User(1,"1097608514", "Emilton Fabian", "Hernandez Mejia", "faherme46@gmail.com", "3166316579", "2005-04-25", "0001abc");
        User u2 = new User(1,"1097608514","Emilton Fabian","Hernandez Mejia","faherme46@gmail.com","3166316578","2005-04-25","0002abc");
        User u3 = new User(1,"1097608514","Emilton Fabian","Hernandez Mejia","faherme46@gmail.com","3166316577","2005-04-25","0003abc");


        users.add(u1);
        users.add(u2);
        users.add(u3);


        return users;
    }

    static int resp=0;
    public void postUser(User u){
        int code;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();

        user.put("isAdmin",u.getIsAdmin());
        user.put("cedula",u.getCedula());
        user.put("nombre",u.getNombre());
        user.put("apellido",u.getApellido());
        user.put("telefono",u.getTelefono());
        user.put("correo",u.getCorreo());
        user.put("fNacimiento",u.getFechaNacimiento());

        db.collection("tokens")
                .add(user)
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


    public User getUserByCc(String cc){
        ArrayList<User> users = getUser();
        for (User u:
             users) {
            if(cc.equals(String.valueOf(u.getCedula()))){
                return u;
            }
        }
        return null;
    }

    private class RetroFit {
    }


    public HelperPersona() {
    }

}
