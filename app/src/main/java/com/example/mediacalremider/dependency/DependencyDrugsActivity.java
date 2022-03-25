package com.example.mediacalremider.dependency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.DrugRecyclerAdapter;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.users.UserController;
import com.example.mediacalremider.users.UserProfile;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DependencyDrugsActivity extends AppCompatActivity implements getDrugsDelegation {
    RecyclerView dependencyDrugsRecyclerView;
   public static List<DrugsModel> dependencyDrugs;
    //DrugsModel drugsModel;
    TextView userName;
    CircleImageView profileImage;
    DrugRecyclerAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependency_drugs);
        dependencyDrugs= new ArrayList<>();
        dependencyDrugsRecyclerView=findViewById(R.id.dependency_drug_recyclerView);
        userName=findViewById(R.id.dependency_user_name);
        profileImage=findViewById(R.id.dependency_user_profile_img);
        String name=getIntent().getStringExtra("name");
        int icon=getIntent().getIntExtra("img",R.drawable.father);
        String id=getIntent().getStringExtra("id");
        userName.setText(name);
        profileImage.setImageResource(icon);
        UserController.getDrugs(id,this);
        myAdapter= new DrugRecyclerAdapter(DependencyDrugsActivity.this,dependencyDrugs);
        LinearLayoutManager layoutManager= new LinearLayoutManager(DependencyDrugsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        dependencyDrugsRecyclerView.setLayoutManager(layoutManager);
        dependencyDrugsRecyclerView.setAdapter(myAdapter);

    }

    @Override
    public void getDrugsSuccessfully(List<DrugsModel> drugs) {
        dependencyDrugs=drugs;
        myAdapter.setMyDrugs(dependencyDrugs);
        myAdapter.notifyDataSetChanged();
    }
}