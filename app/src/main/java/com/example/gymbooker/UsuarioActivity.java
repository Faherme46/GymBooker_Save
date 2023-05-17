package com.example.gymbooker;

import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;
import com.example.gymbooker.Reserva.HelperReservas;
import com.example.gymbooker.Reserva.Reserva;
import com.example.gymbooker.Tokens.HelperToken;
import com.example.gymbooker.Tokens.Tokens;
import com.example.gymbooker.User.User;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UsuarioActivity extends AppCompatActivity {
    private User u;
    private TextView txtname,txtcc,txtfechanacimiento,txttelefono,txttotalreservas,txttoken,txtexpiracion;
    private Tokens token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);


        Intent i= getIntent();
        u= (User) i.getSerializableExtra("user");

        txtname=findViewById(R.id.txtNameUser);
        txtcc=findViewById(R.id.txrCedulaUser);
        txtfechanacimiento=findViewById(R.id.txtFechaUser);
        txttelefono=findViewById(R.id.txtTelefonoUser);
        txttotalreservas=findViewById(R.id.txtReservasUser);
        txttoken=findViewById(R.id.txtTokenUser);
        txtexpiracion=findViewById(R.id.txtTokenExpireUser);

        txtname.setText(u.getNombre());
        txtcc.setText(String.valueOf(u.getCedula()));
        txtfechanacimiento.setText(u.getFechaNacimiento());
        txttelefono.setText(u.getTelefono().toString());
        txttotalreservas.setText(String.valueOf(setReservasTotales()));

        getToken();
        if (token!=null){
            txttoken.setText(token.getTheToken());
            txtexpiracion.setText(token.getfVencimiento());
        }else{
            txttoken.setText("");
            txtexpiracion.setText("");
        }
            }

    public void getToken(){
        HelperToken helperToken;
        helperToken = new HelperToken();
        token= helperToken.getTokenByToken(u.getToken());
    }

    public int setReservasTotales(){
        HelperReservas helperReservas =new HelperReservas();
        ArrayList<Reserva> listReserva,listaFiltrada;
        listaFiltrada=new ArrayList<>();
        listReserva=helperReservas.getReservas();
        for (Reserva r:
             listReserva) {
            if (u.getCedula().equals(r.getCedula())){
                listaFiltrada.add(r);
            }
        }

        return listReserva.size();
    }
}
