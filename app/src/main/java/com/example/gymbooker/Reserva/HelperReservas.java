package com.example.gymbooker.Reserva;

import android.util.Log;

import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.ReservaService;
import com.example.gymbooker.User.User;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HelperReservas {
    ArrayList<Reserva> listReservas = new ArrayList<>();
    public ArrayList<Reserva> getReservas() {

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
                    eachReserva.setEstado((int) item.getValue().get("estado"));
                    eachReserva.setHoraIngreso((String) item.getValue().get("hIngreso"));
                    eachReserva.setHoraSalida((String) item.getValue().get("hSalida"));
                    eachReserva.setRutina((String) item.getValue().get("rutina"));
                    listReservas.add(eachReserva);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        return listReservas;

    }

    public void postReserva(Reserva toPostReserva){

        Retrofit myRetro = APIService.getInstancia();
        ReservaService myReservaService = myRetro.create(ReservaService.class);

        myReservaService.postReserva(toPostReserva).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("mi_log",response.body().toString());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    /*public void updateReserva(Reserva toUpdateReserva){

        Retrofit myRetro = APIService.getInstancia();
        ReservaService myReservaService = myRetro.create(ReservaService.class);

        myReservaService.editReserva(toUpdateReserva.getCedula(),toUpdateReserva).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public void deleteReserva(Reserva toDeleteReserva){

        Retrofit myRetro = APIService.getInstancia();
        ReservaService myReservaService = myRetro.create(ReservaService.class);

        myReservaService.deleteReserva(toDeleteReserva.getCedula()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }*/
}
