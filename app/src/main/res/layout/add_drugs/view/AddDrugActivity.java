package com.example.mediacalremider.drugs.add_drugs.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.model.DrugRefillModel;

public class AddDrugActivity extends AppCompatActivity {

   public static DrugsModel drugsModel;
   public static DrugRefillModel refillModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);

        drugsModel = new DrugsModel();
        refillModel = new DrugRefillModel();

        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.addDrugContainer);
        NavController navController= navHostFragment.getNavController();
        NavGraph navGraph=navHostFragment.getNavController().getNavInflater().inflate(R.navigation.add_drug_navgation_graph);
        navGraph.setStartDestination(R.id.add_drug_first_fragment);
        navController.setGraph(navGraph);
    }
}