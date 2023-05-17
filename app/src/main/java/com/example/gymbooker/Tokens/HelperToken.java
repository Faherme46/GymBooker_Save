package com.example.gymbooker.Tokens;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelperToken {
    ArrayList<Tokens> listToken = new ArrayList<>();
    public ArrayList<Tokens> getTokens() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("cities").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Tokens t1=new Tokens(101,1,"abc1","2023-01-12","2023-05-16",false);
        Tokens t2=new Tokens(102,1,"abc2","2023-01-12","2023-05-16",false);
        Tokens t3=new Tokens(103,1,"abc3","2023-01-12","2023-05-16",false);
        Tokens t4=new Tokens(104,1,"abc4","2023-01-12","2023-05-16",false);
        listToken.add(t1);
        listToken.add(t2);
        listToken.add(t3);
        listToken.add(t4);

        return listToken;
    }

    //Metodo que llama la lista de tokens y busca segun el token indicado
    public Tokens getTokenByToken(String token){
        for (Tokens j:
                getTokens()) {
            if(j.getTheToken().equals(token)){
                return j;
            }
        }
        return null;
    }

    public void postToken(Tokens t){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> token = new HashMap<>();
        token.put("thetoken",t.getTheToken());
        token.put("fCreacion",t.getfCreacion());
        token.put("fVencimiento",t.getfVencimiento());

        db.collection("tokens")
                .add(token)
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

