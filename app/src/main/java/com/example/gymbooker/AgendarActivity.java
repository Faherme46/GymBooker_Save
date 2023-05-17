package com.example.gymbooker;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbooker.R;
import com.example.gymbooker.Reserva.HelperReservas;
import com.example.gymbooker.Reserva.Reserva;


public class AgendarActivity extends AppCompatActivity {
    EditText date, horainicial, horafinal, area;

    private EditText txtrutina, txthora1, txthora2, txtfecha;
    private Button agendar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo crear selector de fecha y hora

        setContentView(R.layout.activity_agendar);

        txtrutina = findViewById(R.id.txtrutina);
        txthora1 = findViewById(R.id.txthora1);
        txthora1 = findViewById(R.id.txthora2);
        txtfecha = findViewById(R.id.txtfecha);
        agendar = findViewById(R.id.btn_agendar);


    }
    //todo añadir selector

    public void guardar(View view) {

        Reserva r = new Reserva();
        r.setFecha(date.getText().toString());
        r.setHoraIngreso(horainicial.getText().toString());
        r.setHoraSalida(horafinal.getText().toString());
        r.setRutina(area.getText().toString());

        r.setDuracion("0");

        HelperReservas helperReservas = new HelperReservas();
        helperReservas.postReserva(r);


    }

}