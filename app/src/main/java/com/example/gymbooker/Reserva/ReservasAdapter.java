package com.example.gymbooker.Reserva;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.R;

import java.util.ArrayList;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ViewHolder> {

    private ArrayList<Reserva> DataSet;
    private onItemClickListener onItemClickListener;
    private int b;
    private ConstraintLayout constraintLayout;

    public void setDataSet(ArrayList<Reserva> dataSet) {
        DataSet = dataSet;
        notifyDataSetChanged();
    }

    public ReservasAdapter(ArrayList<Reserva> dataSet, int b) {
        DataSet = dataSet;
        this.onItemClickListener = null;
        this.b=b;
    }

    public void setOnItemClickListener(ReservasAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ReservasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserva_item, parent, false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservasAdapter.ViewHolder holder, int position) {
        Reserva myres = DataSet.get(position);
        holder.link(myres);
    }

    public int getItemCount() {
        return DataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_fecha,tv_hora,tv_duracion,tv_rutina;
        private Button btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_fecha = itemView.findViewById(R.id.tv_fecha_info);
            tv_hora = itemView.findViewById(R.id.tv_hora_info);
            tv_duracion = itemView.findViewById(R.id.tv_duracion_info);
            tv_rutina = itemView.findViewById(R.id.tv_rutina_info);
            btn = itemView.findViewById(R.id.btn_cancel);
            constraintLayout=itemView.findViewById(R.id.cltItemReserva);

            btn.setVisibility(b);
            if (b==View.INVISIBLE){
                constraintLayout.setMaxHeight(650);
            }
        }

        public void link(Reserva myres){
            tv_fecha.setText(myres.getFecha());
            tv_hora.setText(myres.getHoraIngreso());
            tv_duracion.setText(myres.getDuracion());

            if(onItemClickListener!=null){

                itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        onItemClickListener.onItemClick(myres,getAdapterPosition());
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemBtnClick(myres,getAdapterPosition());
                    }
                });
            }
        }
    }
    public interface onItemClickListener{
        void onItemClick(Reserva myprod, int posicion);
        void onItemBtnClick(Reserva myprod, int posicion);
    }

}
