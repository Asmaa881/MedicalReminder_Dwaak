package com.example.mediacalremider.drugs.add_drugs.model;

import static com.example.mediacalremider.MainActivity.appCurrentUser;
import static com.example.mediacalremider.MainActivity.current_user;
import static com.example.mediacalremider.MainActivity.repo;
import static com.example.mediacalremider.MainActivity.userDrugs;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.dId;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.drugsModel;
import static com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity.requests;
import static com.example.mediacalremider.home.HomeActivity.drugsAdapter1;
import static com.example.mediacalremider.users.UserController.userDocRef;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.mediacalremider.drugs.add_drugs.view.AddDrugActivity;
import com.example.mediacalremider.drugs.add_drugs.view.AddDrugFourthFragment;
import com.example.mediacalremider.drugs.add_drugs.view.ReminderTimesAdapter;
import com.example.mediacalremider.network.InternetConnectionChecker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.Calendar;
import java.util.List;

public class AddDrugRepository implements IModel{

    FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
    String uid = current.getUid();
     FirebaseFirestore db= FirebaseFirestore.getInstance();
     boolean res = false;

    public AddDrugRepository(){
    }

    @Override
    public boolean saveData(CallBack callBack) {

        //drugRandomKey=randomkey();
        FirebaseFirestore.getInstance().collection("Drugs").
                document(dId).set(AddDrugActivity.drugsModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        appCurrentUser.getDrugsIds().add(dId);
                        userDocRef.document(uid).set(appCurrentUser, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                res = true;
                                ReminderTimesAdapter.timesSelected.clear();
                                callBack.callBack(res);
                                String name= AddDrugActivity.drugsModel.getId();
                                List<OneTimeWorkRequest> myrequests=requests;
                                WorkManager.getInstance().enqueueUniqueWork(name,ExistingWorkPolicy.REPLACE,requests);

                                userDrugs.add(drugsModel);
                                drugsAdapter1.setDrugs(userDrugs);
                                drugsAdapter1.notifyDataSetChanged();
                                repo.addDrug(drugsModel);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                res = false;
                callBack.callBack(res);
            }
        });
        return res;
    }




    public interface CallBack{
        void callBack(boolean result);
    }


}
