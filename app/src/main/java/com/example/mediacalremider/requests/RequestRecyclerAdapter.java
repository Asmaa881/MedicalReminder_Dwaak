package com.example.mediacalremider.requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediacalremider.R;
import com.example.mediacalremider.users.UserModel;

import java.util.List;


public class RequestRecyclerAdapter extends RecyclerView.Adapter<RequestRecyclerAdapter.Holder>{
    List<UserModel>users;
    RequestDelegation requestDelegation;


    public void setUsers(List<UserModel> users) {
        this.users = users;
    }

    Context context;
    RequestRecyclerAdapter(Context context,RequestDelegation request){
        this.context=context;
        requestDelegation=request;
    }
    @NonNull
    @Override
    public RequestRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(recyclerView.getContext());
        View v= layoutInflater.inflate(R.layout.request_row,recyclerView,false);
        RequestRecyclerAdapter.Holder viewHolder= new RequestRecyclerAdapter.Holder(v);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull RequestRecyclerAdapter.Holder holder, int position) {
        holder.userName.setText(users.get(position).getName());
        holder.img.setImageResource(users.get(position).getIcon());
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDelegation.cancelRequest(users.get(holder.getAdapterPosition()).getId(),holder.getAdapterPosition());
            }
        });
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDelegation.acceptRequest(users.get(holder.getAdapterPosition()).getId(),holder.getAdapterPosition());
                holder.acceptBtn.setClickable(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class Holder extends RecyclerView.ViewHolder {
        View view;
        TextView userName;
        ImageView img;
        Button acceptBtn;
        Button cancelBtn;

        public Holder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            userName=view.findViewById(R.id.requestUserUserName);
            img=view.findViewById(R.id.requestUserImage);
            cancelBtn=view.findViewById(R.id.cancelBtn);
            acceptBtn=view.findViewById(R.id.acceptBtn);
        }
    }
    }
