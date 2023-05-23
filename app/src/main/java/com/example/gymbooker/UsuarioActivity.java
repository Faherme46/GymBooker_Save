package com.example.gymbooker;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import com.example.gymbooker.Helper.HelperReservas;
import com.example.gymbooker.Class.Reserva;
import com.example.gymbooker.Helper.HelperToken;
import com.example.gymbooker.Class.Tokens;
import com.example.gymbooker.Class.User;
import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.ReservaService;
import com.example.gymbooker.Retrofit.TokensService;

import androidx.appcompat.app.AppCompatActivity;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsuarioActivity extends AppCompatActivity {
    private User u;
    private TextView txtname,txtcc,txtfechanacimiento,txttelefono,txttotalreservas,txttoken,txtexpiracion;
    private Tokens token;
    private HelperToken helperToken = new HelperToken();
    private ArrayList<Tokens> tokensArrayList=new ArrayList<>();
    private ArrayList<Reserva> reservasArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);


        Intent i = getIntent();
        u = (User) i.getSerializableExtra("user");

        txtname = findViewById(R.id.txtNameUser);
        txtcc = findViewById(R.id.txrCedulaUser);
        txtfechanacimiento = findViewById(R.id.txtFechaUser);
        txttelefono = findViewById(R.id.txtTelefonoUser);
        txttotalreservas = findViewById(R.id.txtReservasUser);
        txttoken = findViewById(R.id.txtTokenUser);
        txtexpiracion = findViewById(R.id.txtTokenExpireUser);

        txtname.setText(u.getNombre()+" "+u.getApellido());
        txtcc.setText(String.valueOf(u.getCedula()));
        txtfechanacimiento.setText(u.getFechaNacimiento());
        txttelefono.setText(u.getTelefono().toString());

        setReservasTotales();
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

        Retrofit myRetro = APIService.getInstancia();
        TokensService myTokensService = myRetro.create(TokensService.class);

        myTokensService.getAllTokens().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("mi_log", response.body().toString());

                Map<String, Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()) {
                    Tokens eachToken = new Tokens();
                    eachToken.setId((String) item.getKey());
                    eachToken.setTheToken((String) item.getValue().get("theToken"));
                    eachToken.setfCreacion((String) item.getValue().get("fCreacion"));
                    eachToken.setLimited((double) item.getValue().get("isLimited"));
                    eachToken.setfVencimiento((String) item.getValue().get("fVencimiento"));
                    tokensArrayList.add(eachToken);

                }
                Tokens t1=null;
                for (Tokens tk:
                     tokensArrayList) {
                    if(tk.getTheToken().equals(u.getToken())){
                        t1=tk;
                    }
                }
                txttoken.setText(t1.getTheToken());
                if (t1.getfVencimiento().toString().equals("null")){
                    txtexpiracion.setText("No vence");
                }else{
                    txtexpiracion.setText(t1.getfVencimiento().toString());
                }

            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Fallo",t.getMessage());

            }
        });
    }

    public void setReservasTotales() {
        Retrofit myRetro = APIService.getInstancia();
        ReservaService myReservaService = myRetro.create(ReservaService.class);

        myReservaService.getAllReservas().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                reservasArrayList.clear();

                Map<String, Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()) {
                    Reserva eachReserva = new Reserva();
                    eachReserva.setCedula((String) item.getValue().get("cedula"));
                    eachReserva.setFecha((String) item.getValue().get("fecha"));
                    eachReserva.setDuracion((String) item.getValue().get("duracion"));
                    eachReserva.setEstado((double) item.getValue().get("estado"));
                    eachReserva.setHoraIngreso((String) item.getValue().get("hIngreso"));
                    eachReserva.setHoraSalida((String) item.getValue().get("hSalida"));
                    eachReserva.setRutina((String) item.getValue().get("rutina"));
                    reservasArrayList.add(eachReserva);
                }
                ArrayList<Reserva> listaFiltrada=new ArrayList<>();

                for (Reserva r:
                        reservasArrayList) {
                    if (u.getCedula().equals(r.getCedula())){
                        listaFiltrada.add(r);
                    }

                    txttotalreservas.setText(String.valueOf(listaFiltrada.size()));

            }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }

        });



    }
//
//        return listReserva.size();
}

