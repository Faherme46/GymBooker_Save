package com.example.gymbooker.User;

import android.annotation.SuppressLint;
import android.app.Person;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.HelperFecha;
import com.example.gymbooker.LoginActivity;
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
import java.util.ArrayList;


public class RegisterActivity extends AppCompatActivity {
    //Declarando los arraylist
    private HelperToken helperToken = new HelperToken();
    private ArrayList<Tokens> tokensArrayList = helperToken.getTokens();

    private HelperPersona helperPersona = new HelperPersona();
    private ArrayList<User> userArrayList= helperPersona.getUsers();
    private SharedPreferences preferences;
    private boolean isPrimeraVez=true;

    private TextView txtnombre,txttelefono,txtcorreo,txtcedula,txtfnacimiento,txtApellido;
    private Button btmcontinuar;

    private ImageView back;

    private FirebaseAuth mAuth;
    private BeginSignInRequest signInRequest;

    public RegisterActivity() throws ApiException {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        preferences = getSharedPreferences("gym-booker", MODE_PRIVATE);
        if (preferences.getInt("logged", 0) == 1) {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        txtnombre = findViewById(R.id.ed_nombre);
        txtnombre.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txttelefono = findViewById(R.id.ed_telefono);
        txtcorreo = findViewById(R.id.ed_correo);
        txtApellido=findViewById(R.id.ed_apellido);
        txtApellido.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtcedula = findViewById(R.id.ed_cedula);
        txtfnacimiento = findViewById(R.id.ed_nacimiento);
        btmcontinuar = findViewById(R.id.btn_registrar);

        back = findViewById(R.id.back_register);

        txtfnacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperFecha helperFecha = new HelperFecha();
                helperFecha.mostrarSelectorFecha((TextView) txtfnacimiento);
            }
        });


        //todo quitar metodo de datos por default
        setDatosPruebas();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(back);
            }
        });

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


    public void onClickGuardar(View view) {

        if(verificar()) {
            User u = new User();
            u.setNombre(txtnombre.getText().toString());
            u.setTelefono(txttelefono.getText().toString());
            u.setCorreo(txtcorreo.getText().toString());
            u.setCedula(txtcedula.getText().toString());
            u.setFechaNacimiento(txtfnacimiento.getText().toString());

            String txt1 = getIntent().getStringExtra("txtToken");
            Tokens token1 = helperToken.getTokenByToken(txt1, tokensArrayList);

            if (helperPersona.getUserByCedula(u.getCedula(), userArrayList) == null) {

                //Si es el Admin
                if (preferences.getString("user", "").equals("admin")) {
                    u.setToken(null);
                    //todo implementar aqui el Registro en google
                    BeginSignInRequest.Builder signInRequest;
                    signInRequest = BeginSignInRequest.builder()
                            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                    .setSupported(true)
                                    // Your server's client ID, not your Android client ID.
                                    .setServerClientId(getString(R.string.client))
                                    // Only show accounts previously used to sign in.
                                    .setFilterByAuthorizedAccounts(true)
                                    .build());


                    String idToken = u.getCorreo();
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("creado", "signInWithCredential:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                                        updateUI(null);
                                    }
                                }
                            });

                }
                //si es el Usuario
                else {
                    u.setToken(txt1);
                    token1.setUsed(true);
                    //todo cambiar post a update

                    helperToken.updateToken(token1);
                }
                helperPersona.postUser(u);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("logged", 1);
                editor.putString("ccUsuario", u.getCedula());
                editor.apply();


                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(this, "El Usuario ya existe", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        } else {
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();

        }

    }
    private boolean verificar(){
        if (
                esNulo((EditText) txtnombre) ||
                        esNulo((EditText) txttelefono) ||
                        esNulo((EditText) txtcedula) ||
                        esNulo((EditText) txtcorreo)) {
            return false;
        } else {

            if (!esNumeroDeTelefonoValido(txttelefono.getText().toString())) {
                // El número de teléfono no cumple con los requisitos
                // Realiza las acciones necesarias en caso de validación fallida
                txttelefono.setError("No es un telefono valido");
                return false;
            }
            if (!esCorreoValido(txtcorreo.getText().toString())) {
                // El correo electrónico no cumple con los requisitos
                // Realiza las acciones necesarias en caso de validación fallida
                txtcorreo.setError("No es un correo valido");
                return false;
            }
            if (!esNumeroDeCedulaValido(txtcedula.getText().toString())) {
                // La cédula no cumple con los requisitos
                // Realiza las acciones necesarias en caso de validación fallida
                txtcedula.setError("Solo Pueden ser numeros");
                return false;
            }

            return true;
        }

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

    private boolean esNulo(EditText et) {if(et.getText().toString().isEmpty()){
        et.setError("Complete este campo");
        return true;
    }else{
        return false;
    }

    }

    private void setDatosPruebas(){
        txtcorreo.setText("hernandezmejia159@gmail.com");
        txtnombre.setText("Emilton Fabian");
        txtApellido.setText("Hernandez Mejia");
        txtcedula.setText("1097608514");
        txttelefono.setText("3167017821");
        txtfnacimiento.setText("2005-05-25");

    }

}
