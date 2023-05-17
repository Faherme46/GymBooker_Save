package com.example.gymbooker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;


import com.example.gymbooker.Reserva.HelperReservas;
import com.example.gymbooker.Reserva.Reserva;

import java.util.Calendar;


public class AgendarActivity extends AppCompatActivity {
    EditText date, horainicial, horafinal, area;

    private EditText txtrutina, txthora1, txthora2, txtfecha;
    private Button agendar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_agendar);


        txtrutina = findViewById(R.id.txtrutina);
        txthora1 = findViewById(R.id.txthora1);
        txthora2 = findViewById(R.id.txthora2);
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

        //helperReservas.postReserva(r);



    }

    public class Hora1 extends Activity {
        private TextView textViewHora;
        private boolean isFirstTime = true;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            textViewHora = findViewById(R.id.txthora1);
            textViewHora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarSelectorHora();
                }
            });
        }

        private void mostrarSelectorHora() {
            final Calendar calendar = Calendar.getInstance();
            int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
            int minutoActual = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String horaFormateada = String.format("%02d:%02d", hourOfDay, minute);
                    textViewHora.setText(horaFormateada);
                }
            }, horaActual, minutoActual, true);

            timePickerDialog.show();
        }

        @Override
        protected void onResume() {
            super.onResume();

            // Mostrar el selector de hora solo si es la primera vez que se ejecuta
            if (isFirstTime) {
                isFirstTime = false;
            } else {
                mostrarSelectorHora();
            }
        }

    }

    public class Hora2 extends Activity {
        private TextView textViewHora;
        private boolean isFirstTime = true;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            textViewHora = findViewById(R.id.txthora2);
            textViewHora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarSelectorHora();
                }
            });
        }

        private void mostrarSelectorHora() {
            final Calendar calendar = Calendar.getInstance();
            int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
            int minutoActual = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String horaFormateada = String.format("%02d:%02d", hourOfDay, minute);
                    textViewHora.setText(horaFormateada);
                }
            }, horaActual, minutoActual, true);

            timePickerDialog.show();
        }

        @Override
        protected void onResume() {
            super.onResume();

            // Mostrar el selector de hora solo si es la primera vez que se ejecuta
            if (isFirstTime) {
                isFirstTime = false;
            } else {
                mostrarSelectorHora();
            }
        }

    }
    public class Fecha extends Activity {
        private TextView textViewFecha;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            textViewFecha = findViewById(R.id.txtfecha);
            textViewFecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarSelectorFecha();
                }
            });
        }

        private void mostrarSelectorFecha() {
            final Calendar calendar = Calendar.getInstance();
            int añoActual = calendar.get(Calendar.YEAR);
            int mesActual = calendar.get(Calendar.MONTH);
            int diaActual = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int año, int mes, int dia) {
                    String fechaSeleccionada = año + "-" + (mes + 1) + "-" + dia;
                    textViewFecha.setText(fechaSeleccionada);
                }
            }, añoActual, mesActual, diaActual);

            datePickerDialog.show();
        }
    }
}