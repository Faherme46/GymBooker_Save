package com.example.gymbooker.UserAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbooker.R;
import com.example.gymbooker.User.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private ArrayList<User> listUsers;
    private UsersAdapter.OnItemClickListener onItemClickListener;

    public UsersAdapter(ArrayList<User> listUsers) {
        this.listUsers = listUsers;
    }

    public void setListUsers(ArrayList<User> listUsers) {
        this.listUsers = listUsers;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(UsersAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int i) {
        User u=listUsers.get(i);
        holder.enlazar(u);

    }

    @Override
    public int getItemCount() {
       return listUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre,token,fecha;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre= itemView.findViewById(R.id.txtNameUser);
            token=itemView.findViewById(R.id.txtTokenUser);
            fecha=itemView.findViewById(R.id.txtTokenExpire);
        }
        public void enlazar(User u){
            nombre.setText(u.getNombre());
            token.setText(u.getToken());
            fecha.setText(u.getFechaNacimiento());


            if(onItemClickListener!=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(u,getAdapterPosition());
                    }
                });
        }

        }
    }

    public interface OnItemClickListener{
        void onItemClick(User u, int posicion);
    }
}


