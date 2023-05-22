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
        DocumentReference docRef = db.collection("tokens").document("SF");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot TokenDocument = task.getResult();
                    if (TokenDocument.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + TokenDocument.getData());

                        Map<String, Object> originalMap = TokenDocument.getData();
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
                            Tokens item = new Tokens();

                            item.setIdToken(key);
                            item.setTheToken((String) entry.getValue().get("thtoken"));
                            item.setfCreacion((String) entry.getValue().get("fCreacion"));
                            item.setfVencimiento((String) entry.getValue().get("fVencimiento"));
                            item.setLimited((int) entry.getValue().get("fVencimiento"));

                            listToken.add(item);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }

        });

        return listToken;
    }

    public ArrayList<Tokens> getTokensDefault() {

        ArrayList<Tokens> tokensArrayList=new ArrayList<>();
        Tokens t1=new Tokens(0,"ab01","2023-05-27","2023-05-16","t1",false);
        Tokens t2=new Tokens(0,"ab02","2023-05-27","2023-05-16","t1",false);
        Tokens t3=new Tokens(0,"ab03","2023-05-27","2023-05-16","t1",false);

        tokensArrayList.add(t1);
        tokensArrayList.add(t2);
        tokensArrayList.add(t3);

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

