package com.example.gymbooker.ReservaAdmin;

import android.content.Intent;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.HelperFecha;
import com.example.gymbooker.R;
import com.example.gymbooker.Reserva.HelperReservas;
import com.example.gymbooker.Reserva.Reserva;

import java.util.ArrayList;


public class ReservasDiaActivity extends AppCompatActivity {
    private ArrayList<Reserva> ListaReservas, listaFinal;
    private RecyclerView rvReservas;
    private EditText fechadia;
    private String fechaElegida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_dia);
        HelperFecha helperFecha = new HelperFecha();
        fechaElegida = helperFecha.getFechaActual().toString();
        listaFinal = new ArrayList<>();
        LoadData();
        fechadia = findViewById(R.id.fechaElegidaDiaCaja);
        fechadia.setText(fechaElegida);

    }

    @Override
    protected void onStart() {
        super.onStart();
        rvReservas = findViewById(R.id.rvReservasDia);


        ReservasDiaAdapter myAdapter = new ReservasDiaAdapter(listaFinal);

        myAdapter.setOnItemClickListener(new ReservasDiaAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Reserva myres, int posicion) {
                Intent intent = new Intent(ReservasDiaActivity.this, Reserva.class);
                intent.putExtra("Reserva", myres);
                startActivity(intent);
            }

            @Override
            public void onItemBtnAsisteClick(Reserva myres, int posicion) {
                ListaReservas.remove(posicion);
                myAdapter.setDataSet(ListaReservas);
                //TODO acciones al marcar asistido
                Toast.makeText(ReservasDiaActivity.this, "Asistido", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemBtnCancelaClick(Reserva myprod, int posicion) {
                ListaReservas.remove(posicion);
                myAdapter.setDataSet(ListaReservas);
                //TODO acciones al marcar cancelado
                //todo modificar el estado al cancelar
                Toast.makeText(ReservasDiaActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        rvReservas.setAdapter(myAdapter);
        rvReservas.setLayoutManager(new LinearLayoutManager(this));
    }


    public void LoadData() {

        HelperReservas helperReservas=new HelperReservas();
        ListaReservas=helperReservas.getReservas();
        for (Reserva r:
             ListaReservas) {
            if(r.getFecha().equals(fechaElegida)){

                listaFinal.add(r);
            }

        }

    }

}
