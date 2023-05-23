package com.example.gymbooker.Recycler;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.Helper.HelperFecha;
import com.example.gymbooker.R;
import com.example.gymbooker.Helper.HelperReservas;
import com.example.gymbooker.Class.Reserva;
import com.example.gymbooker.Adapter.ReservasDiaAdapter;
import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.ReservaService;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ReservasDiaActivity extends AppCompatActivity {
    private ArrayList<Reserva> ListaReservas=new ArrayList<>();
    private ArrayList<Reserva> listaFinal;
    private RecyclerView rvReservas;
    private TextView fechadia;
    private String fechaElegida;
    private ReservasDiaAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_dia);
        HelperFecha helperFecha = new HelperFecha();
        fechaElegida = helperFecha.getFechaActual().toString();
        listaFinal = new ArrayList<>();
        LoadData();
        fechadia = findViewById(R.id.fechaElegidaDiaCaja);

        fechadia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helperFecha.mostrarSelectorFecha(fechadia);
                fechaElegida=fechadia.getText().toString();
                fechaElegida=helperFecha.formatoFecha(fechaElegida);
                LoadData();
            }
        });
        fechadia.setText(fechaElegida);

    }

    @Override
    protected void onStart() {
        super.onStart();
        rvReservas = findViewById(R.id.rvReservasDia);


        myAdapter = new ReservasDiaAdapter(listaFinal);

        myAdapter.setOnItemClickListener(new ReservasDiaAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Reserva myres, int posicion) {
                Intent intent = new Intent(ReservasDiaActivity.this, Reserva.class);
                intent.putExtra("Reserva", myres);
                startActivity(intent);
            }
            LayoutInflater inflater = ReservasDiaActivity.this.getLayoutInflater();
            View view= inflater.inflate(R.layout.reserva_item_admin,null);

            @Override
            public void onItemBtnAsisteClick(Reserva myres, int posicion) {
                ListaReservas.remove(posicion);
                myAdapter.setDataSet(ListaReservas);
                //TODO acciones al marcar asistido

                Button bOk= view.findViewById(R.id.btnMark);
                Button cancel= view.findViewById(R.id.btnMark2);
                cancel.setVisibility(View.INVISIBLE);
                Toast.makeText(ReservasDiaActivity.this, "Asistido", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemBtnCancelaClick(Reserva myprod, int posicion) {
                Reserva r=listaFinal.get(posicion);
                r.setEstado(2);
                HelperReservas helperReservas=new HelperReservas();
                helperReservas.postReserva(r);

                listaFinal.remove(posicion);
                myAdapter.setDataSet(ListaReservas);
                //TODO acciones al marcar cancelado
                Button bOk= view.findViewById(R.id.btnMark);
                Button cancel= view.findViewById(R.id.btnMark2);
                bOk.setVisibility(View.INVISIBLE);

                //todo modificar el estado al cancelar
                Toast.makeText(ReservasDiaActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        rvReservas.setAdapter(myAdapter);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));
    }


    public void LoadData() {
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
                myAdapter.setDataSet(ListaReservas);

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        for (Reserva r:
             ListaReservas) {
            if(r.getFecha().equals(fechaElegida)){

                listaFinal.add(r);
            }

        }
        if (listaFinal.size()==0){
            Toast.makeText(this, "No hay reservas en esa fecha", Toast.LENGTH_SHORT).show();
        }

    }

}
