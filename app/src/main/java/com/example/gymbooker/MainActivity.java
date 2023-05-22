package com.example.gymbooker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.seguro_del_token)
                        .setTitle(R.string.token);
                builder.setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        declararToken();
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
    private void declararToken(){

        AlertDialog.Builder builder1= new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view=inflater.inflate(R.layout.caja_token,null);


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
                t.setTheToken(generarToken());
                t.setfCreacion(helperFecha.getFechaActual().toString());
                if (checkBox.isChecked()) {
                    t.isLimited(0);
                    t.setfVencimiento("null");
                } else {
                    t.isLimited(1);
                    t.setfVencimiento(txt.getText().toString());
                }
                t.setUsed(false);


                HelperToken helperToken=new HelperToken();
                helperToken.postToken(t);

                AlertDialog.Builder builder2= new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View view=inflater.inflate(R.layout.caja_token_2,null);
                builder2.setView(view);

                EditText txt;
                txt = view.findViewById(R.id.txtTokenMostrar);
                txt.setEnabled(false);
                txt.setText(t.getTheToken());

                ImageView btnCopy,btnShare;
                btnCopy=view.findViewById(R.id.btnCopy);
                btnShare=view.findViewById(R.id.btnShare);

                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String txt1=t.getTheToken();
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Texto copiado", txt1);
                        if (clipboardManager != null) {
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(MainActivity.this, "Copiado", Toast.LENGTH_SHORT).show();


                    }
                });
                btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo menu share
                        String txt1=t.getTheToken();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, txt1);
                        startActivity(Intent.createChooser(intent, "Compartir usando"));


                    }
                });

                builder2.setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
                builder2.show();

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