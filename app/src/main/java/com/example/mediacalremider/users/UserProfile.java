package com.example.mediacalremider.users;

import static com.example.mediacalremider.MainActivity.appCurrentUser;
import static com.example.mediacalremider.MainActivity.userDrugs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mediacalremider.drugs.DrugRecyclerAdapter;
import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements Delegation{
RecyclerView activeMedicine;
RecyclerView inActiveMedicine;
List<DrugsModel> activeDrugs;
List<DrugsModel>inActiveDrugs;
DrugsModel drugsModel;
TextView userName;
CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        activeMedicine=findViewById(R.id.active_mid);
        inActiveMedicine=findViewById(R.id.inactive_mid);
        userName=findViewById(R.id.userNameTxt);
        profileImage = findViewById(R.id.profile_image);
        activeDrugs =new ArrayList<>();
        inActiveDrugs =new ArrayList<>();
        LinearLayoutManager layoutManager= new LinearLayoutManager(UserProfile.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(UserProfile.this);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);

        if(appCurrentUser!=null){
            userName.setText(appCurrentUser.getName());
            profileImage.setImageResource(appCurrentUser.getIcon());
        }
        else{
            UserController.getUser(this);
        }
        if(userDrugs!=null){
            for(DrugsModel drug:userDrugs){
                if(drug.getState().equals("InActive")){
                    inActiveDrugs.add(drug);
                    Log.i("drug",drug.getState()+ "iiiiiiiin");
                }
                else{
                    activeDrugs.add(drug);
                    Log.i("drug",drug.getState());

                }
            }
        }
        activeMedicine.setLayoutManager(layoutManager);
        DrugRecyclerAdapter myAdapter= new DrugRecyclerAdapter(UserProfile.this, activeDrugs);
        activeMedicine.setAdapter(myAdapter);

        inActiveMedicine.setLayoutManager(layoutManager1);
        DrugRecyclerAdapter myAdapter2= new DrugRecyclerAdapter(UserProfile.this, inActiveDrugs);
        inActiveMedicine.setAdapter(myAdapter2);

    }

    @Override
    public void getUserSuccessListener() {
        userName.setText(appCurrentUser.getName());
        profileImage.setImageResource(appCurrentUser.getIcon());
    }

    @Override
    public void getDependencySuccessListener(List<UserModel>dependencies) {

    }
}