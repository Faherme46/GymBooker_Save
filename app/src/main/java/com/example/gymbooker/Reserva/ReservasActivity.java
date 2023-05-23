package com.example.gymbooker.Reserva;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.HelperFecha;
import com.example.gymbooker.R;
import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.ReservaService;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservasActivity extends AppCompatActivity {
    HelperReservas helperReservas= new HelperReservas();
    private ArrayList<Reserva> ListaReservas=new ArrayList<>();
    private ReservasAdapter myAdapter;
    private ArrayList<Reserva> listaFinal,listaFiltrada;
    private RecyclerView rvReservas;
    private TextView tv_misreservastitle;
    private int b;
    private SharedPreferences preferences;
    private String cc;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);
        preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
        cc=preferences.getString("ccUsuario","");


        LoadData();


    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        Boolean historial= i.getBooleanExtra("historial",false);
        HelperFecha helperFecha=new HelperFecha();
        if (historial){
            listaFinal=helperFecha.fechasPasadas(ListaReservas);

            b=View.INVISIBLE;
        }else{
            preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
            if(preferences.getString("user","user")=="admin"){

                tv_misreservastitle=findViewById(R.id.tv_misreservastitle);
                tv_misreservastitle.setText("RESERVAS");
                b=View.INVISIBLE;
            }
            listaFinal=ListaReservas;

        }

        rvReservas = findViewById(R.id.rv_reservas);
        myAdapter = new ReservasAdapter(listaFinal,b);


        myAdapter.setOnItemClickListener(new ReservasAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Reserva myres, int posicion) {
                Intent intent = new Intent(ReservasActivity.this,Reserva.class);
                intent.putExtra("Reserva",myres);
                startActivity(intent);
            }
            //Boton cancelar
            @Override
            public void onItemBtnClick(Reserva myres, int posicion) {
                ListaReservas.remove(posicion);
                //todo descomentar
                //myAdapter.setDataSet(ListaReservas,cc);
                Toast.makeText(ReservasActivity.this,"Cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        rvReservas.setAdapter(myAdapter);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));
    }

    public void LoadData(){
        ListaReservas.clear();

        Retrofit myRetro = APIService.getInstancia();
        ReservaService myReservaService = myRetro.create(ReservaService.class);

        myReservaService.getAllReservas().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Map<String, Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()){
                    Reserva eachReserva = new Reserva();
                    eachReserva.setCedula((String) item.getValue().get("cedula"));
                    eachReserva.setFecha((String) item.getValue().get("fecha"));
                    eachReserva.setDuracion((String) item.getValue().get("duracion"));
                    eachReserva.setEstado((double) item.getValue().get("estado"));
                    eachReserva.setHoraIngreso((String) item.getValue().get("horaIngreso"));
                    eachReserva.setHoraSalida((String) item.getValue().get("horaSalida"));
                    eachReserva.setRutina((String) item.getValue().get("rutina"));
                    ListaReservas.add(eachReserva);
                }
                Intent i= getIntent();
                Boolean historial= i.getBooleanExtra("historial",false);
                myAdapter.setDataSet(ListaReservas,cc,historial);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }


}