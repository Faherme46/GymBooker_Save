package com.example.gymbooker.Retrofit;

import com.example.gymbooker.Class.Reserva;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReservaService {

    @GET("Reserva.json")
    Call<Object> getAllReservas();

    @POST("Reserva.json")
    Call<Object> postReserva(@Body Reserva toPostReserva);

    /*@PUT("Reserva.json/{key}.json")
    Call<Object> editReserva(@Path ("key") String id,@Body Reserva toUpdateReserva);

    @DELETE("Reserva.json/{key}.json")
    Call<Object> deleteReserva(@Path ("key") String id);*/

}
