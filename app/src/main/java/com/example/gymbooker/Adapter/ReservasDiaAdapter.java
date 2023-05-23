package com.example.gymbooker.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.R;
import com.example.gymbooker.Class.Reserva;
import com.example.gymbooker.Helper.HelperPersona;
import com.example.gymbooker.Class.User;
import com.example.gymbooker.Retrofit.APIService;
import com.example.gymbooker.Retrofit.UserService;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservasDiaAdapter extends RecyclerView.Adapter<ReservasDiaAdapter.ViewHolder> {

    private ArrayList<Reserva> listReserva;
    private ReservasDiaAdapter.onItemClickListener onItemClickListener;



    public void setDataSet(ArrayList<Reserva> dataSet) {
        listReserva = dataSet;
        notifyDataSetChanged();
    }

    public ReservasDiaAdapter(ArrayList<Reserva> listReserva) {
        this.listReserva = listReserva;
       this.onItemClickListener=null;


    }
    public void setOnItemClickListener(ReservasDiaAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserva_item_admin, parent, false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasDiaAdapter.ViewHolder viewHolder, int i) {
        Reserva myres= listReserva.get(i);
        viewHolder.link(myres);
    }


    @Override
    public int getItemCount() {

        return listReserva.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameUser,hInicio,hFin,rutina;
        private Button btnAsiste,btnCancela;
        private ArrayList<User> userArrayList=new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameUser=itemView.findViewById(R.id.txtNameUserReserva);
            hInicio=itemView.findViewById(R.id.txtHoraInicioReserva);
            hFin=itemView.findViewById(R.id.txtHoraFinalReserva);
            rutina=itemView.findViewById(R.id.txtRutinaReservaDia);
            btnAsiste=itemView.findViewById(R.id.btnMark);
            btnCancela=itemView.findViewById(R.id.btnMark2);
        }

        public void link(Reserva myres){
            getUser(nameUser,myres.getCedula());
            rutina.setText(myres.getFecha());

            hInicio.setText(String.valueOf(myres.getHoraIngreso()));
            hFin.setText(String.valueOf(myres.getHoraSalida()));
            if(onItemClickListener!=null){

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        onItemClickListener.onItemClick(myres,getAdapterPosition());
                    }
                });
                btnAsiste.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnCancela.setVisibility(View.INVISIBLE);
                        btnAsiste.setClickable(false);
                        onItemClickListener.onItemBtnAsisteClick(myres,getAdapterPosition());
                    }
                });
                btnCancela.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnAsiste.setVisibility(View.INVISIBLE);
                        btnCancela.setClickable(false);
                        onItemClickListener.onItemBtnCancelaClick(myres,getAdapterPosition());
                    }
                });
            }


        }
        private void getUser(TextView txt,String cc) {

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
                        eachUser.setIsAdmin((String) item.getValue().get("isAdmin"));
                        eachUser.setToken((String) item.getValue().get("thetoken"));
                        userArrayList.add(eachUser);
                    }
                    for (User u:
                         userArrayList) {
                        if(u.getCedula().equals(cc)){
                            txt.setText(u.getNombre().toString());
                        }
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });    }

    }
    public interface onItemClickListener{
        void onItemClick(Reserva myprod, int posicion);
        void onItemBtnAsisteClick(Reserva myprod, int posicion);
        void onItemBtnCancelaClick(Reserva myprod, int posicion);
    }

}
