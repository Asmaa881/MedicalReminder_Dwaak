package com.example.mediacalremider;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.mediacalremider.dependency.getDrugsDelegation;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.home.HomeActivity;
import com.example.mediacalremider.network.InternetConnectionChecker;
import com.example.mediacalremider.notifications.ReminderWorker;
import com.example.mediacalremider.repository.Repository;
import com.example.mediacalremider.users.Delegation;
import com.example.mediacalremider.users.LoginActivity;
import com.example.mediacalremider.users.UserController;
import com.example.mediacalremider.users.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Delegation, getDrugsDelegation {
     FirebaseAuth mAuth;
    public static FirebaseUser current_user;
    public static UserModel appCurrentUser;
    public static List<UserModel>userDependencies=new ArrayList<>();
    public static List<DrugsModel>userDrugs=new ArrayList<>();
    public static Repository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo=new Repository(this);
       mAuth = FirebaseAuth.getInstance();

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        userDependencies=new ArrayList<>();

        if(current_user!=null){
            UserController.getUser(this);
            UserController.getDrugs(current_user.getUid(),this);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(current_user==null){

                    Intent intent= new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent= new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


    }

    @Override
    public void getUserSuccessListener() {
        UserController.getDependent(appCurrentUser,this);
    }

    @Override
    public void getDependencySuccessListener(List<UserModel> dependencies) {
        userDependencies=dependencies;
    }




    @Override
    public void getDrugsSuccessfully(List<DrugsModel> drugs) {
        userDrugs=drugs;
    }
}