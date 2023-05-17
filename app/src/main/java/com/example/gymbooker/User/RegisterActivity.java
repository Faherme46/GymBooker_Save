package com.example.gymbooker.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.MainActivity;
import com.example.gymbooker.R;

public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    private TextView txtnombre,txttelefono,txtcorreo,txtcedula,txtfnacimiento;
    private Button btmcontinuar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtnombre = findViewById(R.id.ed_nombre);
        txttelefono = findViewById(R.id.ed_telefono);
        txtcorreo = findViewById(R.id.ed_correo);
        txtcedula = findViewById(R.id.ed_cedula);
        txtfnacimiento = findViewById(R.id.ed_nacimiento);
        btmcontinuar = findViewById(R.id.btn_registrar);
    }

    public void onClickGuardar(View view){

        HelperPersona bInstance = new HelperPersona();
        User u = new User();
        u.setNombre(txtnombre.getText().toString());
        u.setTelefono(txttelefono.getText().toString());
        u.setCorreo(txtcorreo.getText().toString());
        u.setCedula(txtcedula.getText().toString());
        u.setFechaNacimiento(txtfnacimiento.getText().toString());
        bInstance.postUser(u);

        int response = 0;       //Desvinculé que postUser fuese de return int, para poder subirlo al firestore

        if(response==200){
            Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
            preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
            SharedPreferences.Editor editor= preferences.edit();
            editor.putInt("logged",1);
            editor.putString("ccUsuario",u.getCedula());
            editor.apply();
            Intent i=new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);

        }else if(response==201){
            Toast.makeText(this, "Error al Conectar", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(this, "No carga", Toast.LENGTH_SHORT).show();
        }

    }
}