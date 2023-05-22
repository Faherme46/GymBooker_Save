package com.example.gymbooker.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract  class APIService {
    private static final String URL_Api = "https://gymbooker-c78de-default-rtdb.firebaseio.com/";
    private static Retrofit instance = null;
    public static Retrofit getInstancia() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(URL_Api)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}
