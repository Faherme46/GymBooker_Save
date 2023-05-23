package com.example.gymbooker.Recycler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.MainActivity;
import com.example.gymbooker.R;
import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.UserService;
import com.example.gymbooker.Class.User;
import com.example.gymbooker.Adapter.UsersAdapter;
import com.example.gymbooker.UsuarioActivity;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsersActivity extends AppCompatActivity {

    private ArrayList<User> listUser=new ArrayList<>();

    private RecyclerView recyclerView;
    private ImageButton back;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        recyclerView=findViewById(R.id.rvUsers);
        back=findViewById(R.id.back_users);
        LoadData();

        adapter = new UsersAdapter(listUser);
        adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User u, int posicion) {

                Intent i = new Intent(UsersActivity.this,UsuarioActivity.class);
                i.putExtra("user",u);
                i.putExtra("historial",false);
                startActivity(i);
//               AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
//                builder.setTitle(u.getNombre().toString())
//                        .setItems(R.array.opciones, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // The 'which' argument contains the index position
//                                // of the selected item
//                                Class c = ReservasActivity.class;
//                                Intent j= new Intent(UsersActivity.this,c);
//
//                                Boolean historial=false;
//                                if(which==0) {
//                                    c = ReservasActivity.class;
//                                    historial = true;
//                                }else if(which==1) {
//                                    c = ReservasActivity.class;
//                                    historial = false;
//                                }else if(which==2) {
//                                    c = UsuarioActivity.class;
//                                    j.putExtra("user",u);
//                                    historial = false;
//                                }
//                                j.setClass(UsersActivity.this,c);
//                                j.putExtra("historial",historial);
//                                startActivity(j);
//                            }
//                        });
//                builder.create();
//                builder.show();


            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backing = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backing);
            }
        });
    }



    public void LoadData(){
        listUser.clear();
        Retrofit myRetro = APIService.getInstancia();
        UserService myUserService = myRetro.create(UserService.class);

        myUserService.getAllUsers().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                Map<String, Map> datos = (Map<String, Map>) response.body();

                for (Map.Entry<String, Map> item : datos.entrySet()){
                    User eachUser = new User();
                    eachUser.setCedula((String) item.getValue().get("cedula"));
                    eachUser.setNombre((String) item.getValue().get("nombre"));
                    eachUser.setApellido((String) item.getValue().get("apellido"));
                    eachUser.setTelefono((String) item.getValue().get("telefono"));
                    eachUser.setCorreo((String) item.getValue().get("correo"));
                    eachUser.setFechaNacimiento((String) item.getValue().get("fechaNacimiento"));
                    eachUser.setIsAdmin((String) item.getValue().get("isAdmin"));
                    eachUser.setToken((String) item.getValue().get("token"));
                    listUser.add(eachUser);
                }
                adapter.setListUsers(listUser);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}