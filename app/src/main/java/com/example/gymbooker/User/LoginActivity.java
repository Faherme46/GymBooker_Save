package com.example.gymbooker.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gymbooker.MainActivity;
import com.example.gymbooker.R;
import com.example.gymbooker.Tokens.HelperToken;

public class LoginActivity extends AppCompatActivity {


    private EditText txtuser;
    private Button btnlogin;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        txtuser=findViewById(R.id.txtTokenLogin);
        preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
        int x=preferences.getInt("logged",0);
        if (x==1){
            Intent i=new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    public  void clickIniciar( View view) {

        HelperToken helperToken = new HelperToken();
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("user", "user");
        editor.apply();


        String loginUser = txtuser.getText().toString();

        if (helperToken.getTokenByToken(loginUser) != null) {
            if (helperToken.getTokenByToken(loginUser).isUsed() == false) {

                Intent i = new Intent(this, com.example.gymbooker.User.RegisterActivity.class);
                i.putExtra("txtToken",loginUser);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "El token esta en uso", Toast.LENGTH_SHORT).show();
                txtuser.setError("!");
            }


        }
    }

    public void clickRegistro(View view){

        Intent i = new Intent(this, RegisterActivity.class);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("user", "admin");
        editor.apply();
        startActivity(i);
        finish();
    }

    public void dialogToken(View view){
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(R.string.explicacion_token).setTitle(R.string.token)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }
    }