package com.example.gymbooker.User;


import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.TokensService;
import com.example.gymbooker.Retrofit.UserService;
import com.example.gymbooker.Tokens.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HelperPersona {
    private static final String API_Url =  "https://apex.oracle.com/pls/apex/gymbooker/RESTAPI_GYMBOOKER/";

    ArrayList<User> listUsers = new ArrayList<>();
    public ArrayList<User> getUsers() {

        Retrofit myRetro = APIService.getInstancia();
        UserService myUserService = myRetro.create(UserService.class);

        myUserService.getAllUsers().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Map<String,Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()){
                    User eachUser = new User();
                    eachUser.setCedula((String) item.getValue().get("cedula"));
                    eachUser.setNombre((String) item.getValue().get("nombre"));
                    eachUser.setApellido((String) item.getValue().get("apellido"));
                    eachUser.setTelefono((String) item.getValue().get("telefono"));
                    eachUser.setCorreo((String) item.getValue().get("correo"));
                    eachUser.setFechaNacimiento((String) item.getValue().get("fNacimiento"));
                    eachUser.setIsAdmin((Integer) item.getValue().get("isAdmin"));
                    eachUser.setToken((String) item.getValue().get("thetoken"));
                    listUsers.add(eachUser);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
        return listUsers;
    }

    public void postUser(User toPostUser){

        Retrofit myRetro = APIService.getInstancia();
        UserService myUserService = myRetro.create(UserService.class);

        myUserService.postUser(toPostUser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    /*public void updateUser(User toUpdateUser){

        Retrofit myRetro = APIService.getInstancia();
        UserService myUserService = myRetro.create(UserService.class);

        myUserService.editUser(toUpdateUser.getCedula(),toUpdateUser).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public void deleteUser(User toDeleteUser){

        Retrofit myRetro = APIService.getInstancia();
        UserService myUserService = myRetro.create(UserService.class);

        myUserService.deleteUser(toDeleteUser.getCedula()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }*/

    public User getUserByCc(String cc){
        ArrayList<User> users = getUsers();
        for (User u:
             users) {
            if(cc.equals(String.valueOf(u.getCedula()))){
                return u;
            }
        }
        return null;
    }
}
