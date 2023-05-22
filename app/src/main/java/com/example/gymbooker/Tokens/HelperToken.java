package com.example.gymbooker.Tokens;

import static android.app.PendingIntent.getActivity;

import android.util.Log;
import android.widget.Toast;

import com.example.gymbooker.MainActivity;
import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.TokensService;
import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HelperToken {
    ArrayList<Tokens> listTokens = new ArrayList<>();
    public ArrayList<Tokens> getTokens() {

        Retrofit myRetro = APIService.getInstancia();
        TokensService myTokensService = myRetro.create(TokensService.class);

        myTokensService.getAllTokens().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("mi_log",response.body().toString());

                Map<String,Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()){
                    Tokens eachToken = new Tokens();
                    eachToken.setTheToken((String) item.getValue().get("thetoken"));
                    eachToken.setfCreacion((String) item.getValue().get("fCreacion"));
                    eachToken.setLimited((int) item.getValue().get("isLimited"));
                    eachToken.setfVencimiento((String) item.getValue().get("fVencimiento"));
                    listTokens.add(eachToken);
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

        return listTokens;

    }

    public void postToken(Tokens toPostToken){

        Retrofit myRetro = APIService.getInstancia();
        TokensService myTokensService = myRetro.create(TokensService.class);

        myTokensService.postToken(toPostToken).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }
  public ArrayList<Tokens> getTokensDefault() {

        ArrayList<Tokens> tokensArrayList=new ArrayList<>();
        Tokens t1=new Tokens(0,"ab01","2023-05-27","2023-05-16","t1",false);
        Tokens t2=new Tokens(0,"ab02","2023-05-27","2023-05-16","t1",false);
        Tokens t3=new Tokens(0,"ab03","2023-05-27","2023-05-16","t1",false);

        tokensArrayList.add(t1);
        tokensArrayList.add(t2);
        tokensArrayList.add(t3);

        return listToken;
    }

    /*public void updateToken(Tokens toUpdateToken){

        Retrofit myRetro = APIService.getInstancia();
        TokensService myTokensService = myRetro.create(TokensService.class);

        myTokensService.editToken(toUpdateToken.getTheToken(),toUpdateToken).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

   

    public void deleteToken(Tokens toDeleteToken){

        Retrofit myRetro = APIService.getInstancia();
        TokensService myTokensService = myRetro.create(TokensService.class);

        myTokensService.deleteToken(toDeleteToken.getTheToken()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }*/


    //Metodo que llama la lista de tokens y busca segun el token indicado
    public Tokens getTokenByToken(String token){
        for (Tokens j:
                getTokens()) {
            if(j.getTheToken().equals(token)){
                return j;
            }
        }
        return null;
    }
}

