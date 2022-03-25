package com.example.mediacalremider;

import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.drugsModel;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.requests;
import static com.example.mediacalremider.users.UserController.userDocRef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import android.app.AlertDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.drug_details.DrugDetailsActivity;
import com.example.mediacalremider.notifications.NotificationController;
import com.example.mediacalremider.notifications.ReminderNotification;
import com.example.mediacalremider.notifications.ReminderWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DialogActitvity extends AppCompatActivity {
    Button snooze_btn, take_btn, skip_btn;
    TextView drug_time, med_name, med_strength, med_pills;
    Ringtone r;
    FirebaseFirestore db;
    String medId , medName, medStrength;
    int medPills;
    DrugsModel model;
    FirebaseUser current_user;
    ReminderNotification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_actitvity);

        intialization();
        setDialogData();
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        r= RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        db = FirebaseFirestore.getInstance();
        r.play();
        notification = new ReminderNotification(getApplicationContext());
        snooze_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Data inputData  = new Data.Builder()
                         .putString("medId",medId)
                         .putString("medName",medName)
                         .putString("medStrength",medStrength)
                         .putInt("medPill",medPills).build();
                 OneTimeWorkRequest workRequest = new  OneTimeWorkRequest.Builder(ReminderWorker.class)
                         .setInitialDelay(5, TimeUnit.MINUTES)
                         .setInputData(inputData).build();
                 WorkManager.getInstance().enqueue(workRequest);
                 Toast.makeText(DialogActitvity.this, "snooze 5 minute", Toast.LENGTH_SHORT).show();
                 finish();
                 r.stop();
             }
         });
         take_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 reducePills();
                 finish();
                 r.stop();
             }
         });
         skip_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(DialogActitvity.this, "skiped..", Toast.LENGTH_SHORT).show();
                 finish();
                 r.stop();
                 if(current_user != null) {
                     setState(current_user.getUid());
                 }
             }
         });
    }
    private void setState(String id) {
        HashMap<String,String> state = new HashMap<>();
        state.put("DrugState","Missed");

        userDocRef.document(id).set(state, SetOptions.merge());
    }
    private void intialization() {
        med_name = findViewById(R.id.med_name);
        med_strength = findViewById(R.id.med_strength);
        snooze_btn = findViewById(R.id.snooze_btn);
        take_btn = findViewById(R.id.take_btn);
        skip_btn = findViewById(R.id.skip_btn);
        drug_time = findViewById(R.id.drug_time);
        med_pills = findViewById(R.id.med_pills);
    }
    private void setDialogData(){
        Intent intent = getIntent();
        medId = intent.getStringExtra("medId");
        medName = intent.getStringExtra("medName");
         medStrength = intent.getStringExtra("medStrength");
        medPills = intent.getIntExtra("medPill",1);
        med_name.setText(medName);
        med_strength.setText(medStrength);
        drug_time.setText(getCurrentTime());
        med_pills.append(" "+medPills+ " Pill");
    }
    private String getCurrentTime(){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedTime =dateFormat.format(date);
        Log.i("TAG","Current time : "+ formattedTime);
        return formattedTime;
    }
    private void reducePills(){
        db.collection("Drugs").document(medId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    model = documentSnapshot.toObject(DrugsModel.class);
                    if(model.getCurrentNumOfPills()!= 0){
                    int currentNum = model.getCurrentNumOfPills()-1;
                    model.setCurrentNumOfPills(currentNum);
                    db.collection("Drugs").document(medId).set(model);
                    if(model.getNumOfRefillReminder() == currentNum){
                        NotificationCompat.Builder builder = notification.getRefillReminderChannelNotification();
                        notification.getManager().notify(2,builder.build());
                    }
                    }
                }
            }
        });
    }

    void showDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(DialogActitvity.this);
        AlertDialog dialog=builder.create();
        dialog.setContentView(R.layout.dailog_reminder);
        r.play();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        ImageView imageView= dialog.findViewById(R.id.drug_image);
        Button btnSkipped = dialog.findViewById(R.id.skip_btn);
        Button btnTake =dialog.findViewById(R.id.take_btn);
        Button btnSnooze = dialog.findViewById(R.id.snooze_btn);
        Log.i("dialog","I work");
        dialog.show();
    }
}