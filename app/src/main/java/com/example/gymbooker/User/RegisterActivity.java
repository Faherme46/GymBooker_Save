package com.example.gymbooker.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.MainActivity;
import com.example.gymbooker.R;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.example.gymbooker.Tokens.HelperToken;
import com.example.gymbooker.Tokens.Tokens;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    public boolean exito=false;

    private TextView txtnombre,txttelefono,txtcorreo,txtcedula,txtfnacimiento;
    private Button btmcontinuar;
    @SuppressLint("MissingInflatedId")

    private FirebaseAuth mAuth;
    private BeginSignInRequest signInRequest;

    public RegisterActivity() throws ApiException {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
        if (preferences.getInt("logged",0)==1){
            Intent i=new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        txtnombre = findViewById(R.id.ed_nombre);
        txttelefono = findViewById(R.id.ed_telefono);
        txtcorreo = findViewById(R.id.ed_correo);
        txtcedula = findViewById(R.id.ed_cedula);
        txtfnacimiento = findViewById(R.id.ed_nacimiento);
        btmcontinuar = findViewById(R.id.btn_registrar);



    }


    private void updateUI(FirebaseUser currentUser) {
    }

    public void onClickSignInGoogle(View view){
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();
        mAuth = FirebaseAuth.getInstance();
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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
            BeginSignInRequest.Builder signInRequest;
            signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            // Your server's client ID, not your Android client ID.
                            .setServerClientId(getString(R.string.client))
                            // Only show accounts previously used to sign in.
                            .setFilterByAuthorizedAccounts(true)
                            .build());

        }else{
            u.setToken(t1);
            token1.setUsed(true);
            //todo cambiar post a update
            helperToken.postToken(token1);
        }
        bInstance.postUser(u);
        String idToken = null;
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d( "creado", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                        int response = 0;       //Desvinculé que postUser fuese de return int, para poder subirlo al firestore

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
                });
    }


    private void verificar(User u) {
        esNulo((EditText) txtnombre);
        if (!esNumeroDeTelefonoValido(u.getTelefono())) {
            // El número de teléfono no cumple con los requisitos
            // Realiza las acciones necesarias en caso de validación fallida
        }

        if (!esCorreoValido(u.getCorreo())) {
            // El correo electrónico no cumple con los requisitos
            // Realiza las acciones necesarias en caso de validación fallida
        }

        if (!esNumeroDeCedulaValido(u.getCedula())) {
            // La cédula no cumple con los requisitos
            // Realiza las acciones necesarias en caso de validación fallida
        }

        if (!esFechaValida(u.getFechaNacimiento())) {
            // La fecha de nacimiento no cumple con los requisitos
            // Realiza las acciones necesarias en caso de validación fallida
        }

        // Todas las validaciones han sido exitosas
        // Realiza las acciones necesarias en caso de validación exitosa
    }

    private boolean esNumeroDeTelefonoValido(String telefono) {
        // Verifica que el número de teléfono tenga máximo 10 caracteres y sean solo números
        return telefono.matches("\\d{1,10}");
    }

    private boolean esCorreoValido(String correo) {
        // Verifica que el correo electrónico tenga un formato válido
        return android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches();
    }

    private boolean esNumeroDeCedulaValido(String cedula) {
        // Verifica que la cédula sean solo números
        return cedula.matches("\\d+");
    }

    private boolean esFechaValida(String fecha) {
        // Realiza la validación de que la fecha tenga un formato válido
        // Puedes utilizar librerías como SimpleDateFormat para validar el formato y la existencia de la fecha
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            dateFormat.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void esNulo(EditText et) {
        if(et.getText().toString().isEmpty()){
            et.setError("Complete este campo");
        }
    }


}
