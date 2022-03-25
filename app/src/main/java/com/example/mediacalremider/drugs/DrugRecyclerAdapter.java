package com.example.mediacalremider.drugs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.drug_details.DrugDetailsActivity;

import java.util.List;

public class DrugRecyclerAdapter extends RecyclerView.Adapter<DrugRecyclerAdapter.Holder>{
    public void setMyDrugs(List<DrugsModel> myDrugs) {
        this.myDrugs = myDrugs;
    }

    List<DrugsModel> myDrugs;
    Context context;
    public DrugRecyclerAdapter(Context context, List<DrugsModel> drugs){
        myDrugs=drugs;
        this.context=context;
    }

    @NonNull
    @Override
    public DrugRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(recyclerView.getContext());
        View v= layoutInflater.inflate(R.layout.acivity_drugitem,recyclerView,false);
        Holder viewHolder= new Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrugRecyclerAdapter.Holder holder,  @SuppressLint("RecyclerView") final int position) {
        holder.img.setImageResource(myDrugs.get(position).getIcon());
        holder.numOfPills.setText(String.valueOf(myDrugs.get(position).getNumOfRefillReminder())+"Pills left");
        holder.drugName.setText(myDrugs.get(position).getName());
        holder.drugStrength.setText(myDrugs.get(position).getStrength());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DrugDetailsActivity.class);
                intent.putExtra("drugDetails", myDrugs.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDrugs.size();
    }


    class Holder extends RecyclerView.ViewHolder{
        View view;
        CardView linearLayout;
        TextView drugName;
        TextView  drugStrength;
        TextView  numOfPills;
        ImageView img;

        public Holder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            drugName=view.findViewById(R.id.drug_name);
            drugStrength=view.findViewById(R.id.size);
            numOfPills=view.findViewById(R.id.pills);
            img=view.findViewById(R.id.drug_icon);
            linearLayout=view.findViewById(R.id.constraintLayoutRecyclerviewDrugItem);
        }
    }
}
