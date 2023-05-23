package com.example.gymbooker.Retrofit;

import com.example.gymbooker.Class.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

    @GET("Persona.json")
    Call<Object> getAllUsers();

    @POST("Persona.json")
    Call<Object> postUser(@Body User toPostUser);

    /*@PUT("Persona.json/{key}.json")
    Call<Object> editUser(@Path ("key") String id,@Body User toUpdateUser);

    @DELETE("Persona.json/{key}.json")
    Call<Object> deleteUser(@Path ("key") String id);*/

}
