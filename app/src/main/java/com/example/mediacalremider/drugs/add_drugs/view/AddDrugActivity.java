package com.example.mediacalremider.drugs.add_drugs.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkRequest;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.model.DrugRefillModel;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDrugActivity extends AppCompatActivity {

   public static DrugsModel drugsModel;
   public static DrugRefillModel refillModel;
    public static List<OneTimeWorkRequest> requests;
    public static ProgressDialog progressDialog1;
    private String saveCurrentDate, saveCurrentTime, drugRandomKey;
    public  static String dId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
        drugsModel = new DrugsModel();
        refillModel = new DrugRefillModel();
        requests= new ArrayList<>();
        dId = randomkey();
        drugsModel.setId(dId);

        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.addDrugContainer);
        NavController navController= navHostFragment.getNavController();
        NavGraph navGraph=navHostFragment.getNavController().getNavInflater().inflate(R.navigation.add_drug_navgation_graph);
        navGraph.setStartDestination(R.id.add_drug_first_fragment);
        navController.setGraph(navGraph);
    }
    private String randomkey(){
        Calendar calendar = Calendar.getInstance();
        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        java.text.SimpleDateFormat currentTime = new java.text.SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        drugRandomKey = saveCurrentDate + saveCurrentTime;
        return drugRandomKey;
    }

}