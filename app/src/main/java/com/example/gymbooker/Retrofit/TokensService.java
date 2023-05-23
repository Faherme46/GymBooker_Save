package com.example.gymbooker.Retrofit;

import com.example.gymbooker.Class.Tokens;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TokensService {

    @GET("Tokens.json")
    Call<Object> getAllTokens();

    @POST("Tokens.json")
    Call<Object> postToken(@Body Tokens miToken);

    @PUT("Tokens.json/{key}.json")
    Call<Object> editToken(@Path ("key") String id,@Body Tokens updatedToken);

    @DELETE("Tokens.json/{key}.json")
    Call<Object> deleteToken(@Path ("key") String id);

}
