package com.example.gymbooker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.gymbooker.Reserva.HelperReservas;
import com.example.gymbooker.Reserva.Reserva;

import java.util.Calendar;


public class AgendarActivity extends AppCompatActivity {

    private boolean isFirstTimeH1 = true;
    private boolean isFirstTimeH2 = true;
    private HelperFecha helperFecha=new HelperFecha();
    private EditText txtrutina;
    private TextView txthora1, txthora2, txtfecha;
    private ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_agendar);


        txtrutina = findViewById(R.id.txtrutina);
        txthora1 = findViewById(R.id.txthora1);
        txthora2 = findViewById(R.id.txthora2);
        txtfecha = findViewById(R.id.txtfecha);
        back = findViewById(R.id.back_agendar);
        Button agendar = findViewById(R.id.btn_agendar);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(AgendarActivity.this,MainActivity.class);
                startActivity(back);
            }
        });
        txthora1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarSelectorHora();
            }
        });
        txthora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarSelectorHora();
            }
        });
        txtfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                helperFecha.mostrarSelectorFecha(txtfecha);
            }
        });
        //todo quitar metodo
        //metodo pora mostrar datos default de prueba
        datosDefault();

    }

    public void onClickGuardarReserva(View view) {
        SharedPreferences preferences = getSharedPreferences("gym-booker", MODE_PRIVATE);

        Reserva r = new Reserva();
        r.setFecha(txtfecha.getText().toString());
        r.setHoraIngreso(txthora1.getText().toString());
        r.setHoraSalida(txthora2.getText().toString());
        r.setRutina(txtrutina.getText().toString());
        r.setCedula(preferences.getString("ccUsuario",""));
        r.setDuracion(helperFecha.entreHoras(txthora1.getText().toString(),txthora2.getText().toString()));

        HelperReservas helperReservas = new HelperReservas();
        helperReservas.postReserva(r);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"denis.fedi@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "GYMBOOKER - Reserva exitosa!");
        i.putExtra(Intent.EXTRA_TEXT   , "Acabas de realizar una reserva a través de la aplicación GymBooker. A continuación puedes ver la información de tu reserva. Feliz entrenamiento!" +
                                                "\n\n\n\nFecha: " + r.getFecha().toString()
                                             + "\n\nHora de ingreso: " + r.getHoraIngreso()
                                             + "\n\nHora de salida: " + r.getHoraSalida()
                                             + "\n\nRutina: " + r.getRutina()
                                             + "\n\nDuración: " + r.getDuracion());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AgendarActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(this,MainActivity.class);
        startActivity(i);
    }

        private void mostrarSelectorHora() {
            final Calendar calendar = Calendar.getInstance();
            int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
            int minutoActual = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String horaFormateada = String.format("%02d:%02d", hourOfDay, minute);
                    txthora1.setText(horaFormateada);
                }
            }, horaActual, minutoActual, true);

            timePickerDialog.show();
        }
        @Override
        protected void onResume() {
            super.onResume();

            // Mostrar el selector de hora solo si es la primera vez que se ejecuta
            if (isFirstTimeH1) {
                isFirstTimeH1 = false;
            } else {
                mostrarSelectorHora();
            }


            if (isFirstTimeH2) {
                isFirstTimeH2 = false;
            } else {
                mostrarSelectorHora();
            }
    }
    //todo evitar que ponga una hora inicial despues de la final

    private void datosDefault(){
        txtrutina.setText("Abdomen");
        txthora1.setText("16:00");
        txthora2.setText("18:00");
        txtfecha.setText("2023-05-17");
    }


 }
