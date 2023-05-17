package com.example.gymbooker.ReservaAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.R;
import com.example.gymbooker.Reserva.Reserva;
import com.example.gymbooker.User.HelperPersona;
import com.example.gymbooker.User.User;

import java.util.ArrayList;

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
            HelperPersona helperPersona=new HelperPersona();
            User u=helperPersona.getUserByCc(myres.getCedula());
            if (u!=null){
                nameUser.setText(u.getNombre());
            }else{
                nameUser.setText("Nemo Nobody");
            }
            rutina.setText(myres.getRutina());

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

    }
    public interface onItemClickListener{
        void onItemClick(Reserva myprod, int posicion);
        void onItemBtnAsisteClick(Reserva myprod, int posicion);
        void onItemBtnCancelaClick(Reserva myprod, int posicion);
    }
}
