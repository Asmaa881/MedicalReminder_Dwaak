package com.example.mediacalremider.drugs.add_drugs.model;

import androidx.annotation.NonNull;

import com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddDrugRepository implements IModel{

    private String saveCurrentDate, saveCurrentTime, drugRandomKey;

    public AddDrugRepository(){
        drugRandomKey = randomkey();
    }
    @Override
    public void saveData() {
        saveDrugData();
    }
    private void saveDrugData(){
        drugRandomKey=randomkey();
        FirebaseFirestore.getInstance().collection("Drugs").
                document(drugRandomKey).set(AddDrugActivity.drugsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
    private String randomkey(){
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        java.text.SimpleDateFormat currentTime = new java.text.SimpleDateFormat("HH:mm:ss a");  // 12 hours
        saveCurrentTime = currentTime.format(calendar.getTime());
        drugRandomKey = saveCurrentDate + saveCurrentTime;
        return drugRandomKey;
    }




}
