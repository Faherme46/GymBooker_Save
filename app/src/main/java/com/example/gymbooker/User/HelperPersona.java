package com.example.gymbooker.User;


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

public class HelperPersona {
    private static final String API_Url =  "https://apex.oracle.com/pls/apex/gymbooker/RESTAPI_GYMBOOKER/";

    ArrayList<User> listUsers = new ArrayList<>();

    public ArrayList<User> getUsers() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("persona").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot UserDocument = task.getResult();
                    if (UserDocument.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + UserDocument.getData());

                        Map<String, Object> originalMap = UserDocument.getData();
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
                            User item = new User();

                            item.setCedula((String) entry.getValue().get("cedula"));
                            item.setNombre((String) entry.getValue().get("nombre"));
                            item.setApellido((String) entry.getValue().get("apellido"));
                            item.setCorreo((String) entry.getValue().get("correo"));
                            item.setTelefono((String) entry.getValue().get("telefono"));
                            item.setFechaNacimiento((String) entry.getValue().get(""));
                            item.setIsAdmin((int) entry.getValue().get("isAdmin"));

                            listUsers.add(item);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }

        });


        User u1 = new User(1,"1097608514", "Emilton Fabian", "Hernandez Mejia", "faherme46@gmail.com", "3166316579", "2005-04-25", "0001abc");
        User u2 = new User(1,"1097608514","Emilton Fabian","Hernandez Mejia","faherme46@gmail.com","3166316578","2005-04-25","0002abc");
        User u3 = new User(1,"1097608514","Emilton Fabian","Hernandez Mejia","faherme46@gmail.com","3166316577","2005-04-25","0003abc");

        listUsers.add(u1);
        listUsers.add(u2);
        listUsers.add(u3);

        return listUsers;
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
        ArrayList<User> users = getUsers();
        for (User u:
             users) {
            if(cc.equals(String.valueOf(u.getCedula()))){
                return u;
            }
        }
        return null;
    }

    public HelperPersona() {
    }

}
