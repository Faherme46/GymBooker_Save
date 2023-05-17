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

import java.util.ArrayList;

public class ReservasActivity extends AppCompatActivity {

    private ArrayList<Reserva> ListaReservas,listaFinal,listaFiltrada;
    private RecyclerView rvReservas;
    private TextView tv_misreservastitle;
    private int b;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);

        LoadData();

        Intent i = getIntent();
        Boolean historial= i.getBooleanExtra("historial",false);
        HelperFecha helperFecha=new HelperFecha();
        if (historial){
            listaFinal=helperFecha.fechasPasadas(listaFiltrada);

            b=View.INVISIBLE;
        }else{
            preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
            if(preferences.getString("user","user")=="admin"){

                tv_misreservastitle=findViewById(R.id.tv_misreservastitle);
                tv_misreservastitle.setText("RESERVAS");
                b=View.INVISIBLE;
            }
            listaFinal=helperFecha.fechasFuturas(listaFiltrada);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        rvReservas = findViewById(R.id.rv_reservas);
        ReservasAdapter myAdapter = new ReservasAdapter(listaFinal,b);

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
                myAdapter.setDataSet(ListaReservas);
                Toast.makeText(ReservasActivity.this,"Cancelada", Toast.LENGTH_SHORT).show();
            }
        });

        rvReservas.setAdapter(myAdapter);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));
    }

    public void LoadData(){
        HelperReservas helperReservas= new HelperReservas();
        ListaReservas=helperReservas.getReserva();
        preferences=getSharedPreferences("gym-booker",MODE_PRIVATE);
        String cc=preferences.getString("ccUsuario","");
        listaFiltrada=new ArrayList<>();
        for (Reserva r:
             ListaReservas) {
            if (r.getCedula().equals(cc)){
                listaFiltrada.add(r);
            }
        }
    }

}