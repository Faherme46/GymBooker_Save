package com.example.gymbooker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.Reserva.ReservasActivity;
import com.example.gymbooker.ReservaAdmin.ReservasDiaActivity;
import com.example.gymbooker.Tokens.HelperToken;
import com.example.gymbooker.Tokens.Tokens;

import com.example.gymbooker.UserAdmin.UsersActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    private Button agendar,miEntreno,historial,config,reservas,usuarios,generar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
        if(preferences.getString("user","").equals("user")){
            setContentView(R.layout.activity_main);
            startUser();
        }else{
            setContentView(R.layout.activity_main_admin);
            startAdmin();
        }

    }


    private void startUser() {
        agendar = findViewById(R.id.btnReservasDia);
        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, AgendarActivity.class);
                startActivity(i);
            }
        });
        miEntreno = findViewById(R.id.btnMisEntrenamientos);
        miEntreno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, ReservasActivity.class);
                i.putExtra("historial",false);
                startActivity(i);
            }
        });
        historial = findViewById(R.id.btnVerUsuarios);
        historial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, ReservasActivity.class);
                i.putExtra("historial",true);
                startActivity(i);
            }
        });
    }

    private  void startAdmin(){
        generar=findViewById(R.id.btnGenerarToken);
        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token=null;
                HelperToken helperToken=new HelperToken();
                ArrayList<Tokens> listToken= helperToken.getTokens();
                do{
                    token=generarToken();
                }while (helperToken.getTokenByToken(token)!=null);
                HelperFecha helperFecha=new HelperFecha();
                Tokens t=new Tokens();
                t.setTheToken(token);
                t.setfCreacion(helperFecha.getFechaActual().toString());
                //todo cuadrar fechas con Dialog
                boolean isUnlimited=true;
                LocalDate date=null;
                if (isUnlimited){
                    t.isLimited(0);
                    t.setfVencimiento("null");
                }else {
                    t.isLimited(1);
                    t.setfVencimiento(date.toString());
                }


                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> user = new HashMap<>();
                user.put("thetoken",t.getTheToken());
                user.put("fCreacion",t.getfCreacion());
                user.put("fVencimiento",t.getfVencimiento());

                db.collection("users")
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

                    AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.seguro_del_token)
                            .setTitle(R.string.token);
                    String finalToken = token;
                    builder.setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            declararToken(finalToken);
                        }
                    });
                    builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
            }
        });
        config= findViewById(R.id.btnConfig);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        usuarios= findViewById(R.id.btnVerUsuarios);
        usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, UsersActivity.class);
                startActivity(i);
            }
        });
        reservas= findViewById(R.id.btnReservasDia);
        reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, ReservasDiaActivity.class);
                startActivity(i);
            }
        });
    }
    private String generarToken(){
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.:!";


        String cadena = "";
       for (int x = 0; x < 12; x++) {
           int indiceAleatorio = ThreadLocalRandom.current().nextInt(0, banco.length() + 1);
           char caracterAleatorio = banco.charAt(indiceAleatorio);
           cadena += caracterAleatorio;
       }
        return cadena;
    }
    private void declararToken(String token){

        AlertDialog.Builder builder1= new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view=inflater.inflate(R.layout.caja_token,null);
        String finalToken = token;

        builder1.setView(view);
        EditText txt;
        txt = view.findViewById(R.id.fechaElegidaDiaCaja);
        TextView mensaje=view.findViewById(R.id.txFechaexpire);

        CheckBox checkBox=view.findViewById(R.id.noVenceCaja);
        checkBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        txt.setEnabled(!checkBox.isChecked());
                        if(checkBox.isChecked()){
                            mensaje.setTextColor(R.color.gray);
                        }else{
                            mensaje.setTextColor(R.color.black);
                        }
                    }
                }
        );
        builder1.setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                HelperFecha helperFecha = new HelperFecha();
                Tokens t = new Tokens();
                t.setTheToken(token);
                t.setfCreacion(helperFecha.getFechaActual().toString());

                t.setfVencimiento(txt.getText().toString());
                boolean isUnlimited = true;
                LocalDate date = null;
                if (isUnlimited) {
                    t.isLimited(0);
                    t.setfVencimiento("null");
                } else {
                    t.isLimited(1);
                    t.setfVencimiento(date.toString());
                }
            }
        }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        AlertDialog alertDialog=builder1.create();
        alertDialog.show();




    }
}