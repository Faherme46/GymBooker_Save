package com.example.gymbooker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.TokensService;
import com.example.gymbooker.Helper.HelperToken;
import com.example.gymbooker.Class.Tokens;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {


    private EditText txtuser;
    private Button btnlogin;
    private SharedPreferences preferences;
    private HelperToken helperToken = new HelperToken();
    public ArrayList<Tokens> tokensArrayList=new ArrayList<>();

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

    @Override
    protected void onStart() {
        super.onStart();
        loadTokens();
    }

    public  void clickIniciar(View view) {


        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("user", "user");
        editor.apply();

        if(txtuser.getText().toString().isEmpty()) {
            Toast.makeText(this, "Ingrese el Token", Toast.LENGTH_SHORT).show();
        }else {
            String loginUser = txtuser.getText().toString();
            Tokens t1 = helperToken.getTokenByToken(loginUser,tokensArrayList);

            if (t1 != null) {
                if (t1.isUsed() == false) {
                    Intent i = new Intent(this, RegisterActivity.class);
                    i.putExtra("txtToken", loginUser);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "El token esta en uso", Toast.LENGTH_SHORT).show();
                    txtuser.setError("!");
                }
            } else {
                Toast.makeText(this, "El token no existe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickAdmin(View view){

        Intent i = new Intent(this, MainActivity.class);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("user", "admin");
        editor.putInt("logged",1);
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
    private void loadTokens(){
        tokensArrayList.clear();
        Retrofit myRetro = APIService.getInstancia();
        TokensService myTokensService = myRetro.create(TokensService.class);

        myTokensService.getAllTokens().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("mi_log",response.body().toString());

                Map<String, Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()){
                    Tokens eachToken = new Tokens();
                    eachToken.setId((String) item.getKey());
                    eachToken.setTheToken((String) item.getValue().get("theToken"));
                    eachToken.setfCreacion((String) item.getValue().get("fCreacion"));
                    eachToken.setLimited((double) item.getValue().get("isLimited"));
                    eachToken.setfVencimiento((String) item.getValue().get("fVencimiento"));
                    tokensArrayList.add(eachToken);

                }

            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Fallo",t.getMessage());

            }
        });

    }
    }