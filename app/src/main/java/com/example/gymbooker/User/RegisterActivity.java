package com.example.gymbooker.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.MainActivity;
import com.example.gymbooker.R;
import com.example.gymbooker.Tokens.HelperToken;
import com.example.gymbooker.Tokens.Tokens;

public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    public boolean exito=false;

    private TextView txtnombre,txttelefono,txtcorreo,txtcedula,txtfnacimiento;
    private Button btmcontinuar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
        if (preferences.getInt("logged",0)==1){
            Intent i=new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

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
        String t1=getIntent().getStringExtra("txtToken");

        HelperToken helperToken=new HelperToken();
        Tokens token1 =helperToken.getTokenByToken(t1);
        if (preferences.getString("user","")=="admin"){
            u.setToken(null);
            //todo implementar aqui el Registro en google
            String correo= u.getCorreo();

        }else{
            u.setToken(t1);
            token1.setUsed(true);
            //todo cambiar post a update
            helperToken.postToken(token1);
        }
        bInstance.postUser(u);

        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt("logged",1);
        editor.putString("ccUsuario",u.getCedula());
        editor.apply();
        //Actualizar token

        Intent i=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
        finish();
//        }else {
//            Toast.makeText(this, "Error al Conectar", Toast.LENGTH_SHORT).show();
//            Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
//            startActivity(i);
//        }



    }

    private  void verificar(){
        esNulo((EditText) txtnombre);
        //todo las verificaciones de cedula, de telefono, de correo, de contraseña
    }

    private void esNulo(EditText et) {
        if(et.getText().toString().isEmpty()){
            et.setError("Complete este campo");
        }
    }
}