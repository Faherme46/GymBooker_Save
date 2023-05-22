package com.example.gymbooker.User;


import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

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

        return listUsers;
    }

    public ArrayList<User> getUserDefault() {
        ArrayList<User> userArrayList=new ArrayList<>();

        User u1=new User(0,"1097608514","3167017821","Emilton","Hernandez","faherme46@gmail.com","2005-04-25","");
        User u2=new User(0,"1100222657","3167017821","Deniss","Diaz","faherme46@gmail.com","2005-04-25","");
        User u3=new User(0,"1098653245","3167017821","Camilo","Yepes","faherme46@gmail.com","2005-04-25","");

        userArrayList.add(u1);
        userArrayList.add(u2);
        userArrayList.add(u3);

        return userArrayList;
    }


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
        user.put("token",u.getToken());

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
