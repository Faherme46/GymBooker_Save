package com.example.gymbooker.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.MainActivity;
import com.example.gymbooker.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    private TextView txtnombre,txttelefono,txtcorreo,txtcedula,txtfnacimiento;
    private Button btmcontinuar;
    @SuppressLint("MissingInflatedId")

    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        txtnombre = findViewById(R.id.ed_nombre);
        txttelefono = findViewById(R.id.ed_telefono);
        txtcorreo = findViewById(R.id.ed_correo);
        txtcedula = findViewById(R.id.ed_cedula);
        txtfnacimiento = findViewById(R.id.ed_nacimiento);
        btmcontinuar = findViewById(R.id.btn_registrar);
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
    }

    public void onClickGuardar(View view){
        BeginSignInRequest.Builder signInRequest;
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.client))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build());
        HelperPersona bInstance = new HelperPersona();
        User u = new User();
        u.setNombre(txtnombre.getText().toString());
        u.setTelefono(txttelefono.getText().toString());
        u.setCorreo(txtcorreo.getText().toString());
        u.setCedula(txtcedula.getText().toString());
        u.setFechaNacimiento(txtfnacimiento.getText().toString());
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

                        if(response==200){
                            //todo Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
                            SharedPreferences.Editor editor= preferences.edit();
                            editor.putInt("logged",1);
                            editor.putString("ccUsuario",u.getCedula());
                            editor.apply();
                            Intent i=new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(i);

                        }else if(response==201){
                            //todo Toast.makeText(this, "Error al Conectar", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        }else {
                            //todo Toast.makeText(this, "No carga", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}
