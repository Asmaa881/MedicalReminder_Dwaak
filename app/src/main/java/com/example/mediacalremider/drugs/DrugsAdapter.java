package com.example.mediacalremider.drugs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.drug_details.DrugDetailsActivity;
import com.example.mediacalremider.home.RecyclerViewOnDrugClickListener;

import java.util.ArrayList;
import java.util.List;

public class DrugsAdapter extends RecyclerView.Adapter<DrugsAdapter.ViewHolder> {
    Context context;
    private List<DrugsModel> drugs;

    public void setDrugs(List<DrugsModel> drugs) {
        this.drugs = drugs;

    }
    public DrugsAdapter(List<DrugsModel> drug, Context context) {
        this.drugs = drug ;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.acivity_drugitem, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
    DrugsModel drug = drugs.get(position);
    holder.drug_icon.setImageResource(drug.getIcon());
    holder.drug_name.setText(drug.getName());
    holder.drug_time.setText(String.valueOf(drug.getTimes()));
    holder.pills.setText(String.valueOf(drug.getNumOfPills()));
    holder.size.setText(String.valueOf(drug.getStrength()));
    holder.constraintLayoutRecyclerviewDrugItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,DrugDetailsActivity.class);
            intent.putExtra("drugDetails", drugs.get(position));
            intent.putExtra("drugPosition",position);
            context.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView drug_icon;
        TextView drug_name,drug_time,size,pills;
        CardView constraintLayoutRecyclerviewDrugItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drug_time = itemView.findViewById(R.id.drug_time);
            drug_icon = itemView.findViewById(R.id.drug_icon);
            drug_name = itemView.findViewById(R.id.drug_name);
            pills = itemView.findViewById(R.id.pills);
            size = itemView.findViewById(R.id.size);
            constraintLayoutRecyclerviewDrugItem = itemView.findViewById(R.id.constraintLayoutRecyclerviewDrugItem);

        }
    }
}



