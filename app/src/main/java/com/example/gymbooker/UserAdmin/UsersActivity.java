package com.example.gymbooker.UserAdmin;

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
import com.example.gymbooker.Reserva.ReservasActivity;
import com.example.gymbooker.User.HelperPersona;
import com.example.gymbooker.User.User;
import com.example.gymbooker.UsuarioActivity;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private ArrayList<User> listUser;

    private RecyclerView recyclerView;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        recyclerView=findViewById(R.id.rvUsers);
        back=findViewById(R.id.back_users);
        LoadData();

        UsersAdapter adapter = new UsersAdapter(listUser);
        adapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(User u, int posicion) {
               AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                builder.setTitle(u.getNombre().toString())
                        .setItems(R.array.opciones, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                Class c = ReservasActivity.class;
                                Intent j= new Intent(UsersActivity.this,c);

                                Boolean historial=false;
                                if(which==0) {
                                    c = ReservasActivity.class;
                                    historial = true;
                                }else if(which==1) {
                                    c = ReservasActivity.class;
                                    historial = false;
                                }else if(which==2) {
                                    c = UsuarioActivity.class;
                                    j.putExtra("user",u);
                                    historial = false;
                                }
                                j.setClass(UsersActivity.this,c);
                                j.putExtra("historial",historial);
                                startActivity(j);
                            }
                        });
                builder.create();
                builder.show();


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
        HelperPersona helperPersona=new HelperPersona();
        listUser=helperPersona.getUsers();
    }
}