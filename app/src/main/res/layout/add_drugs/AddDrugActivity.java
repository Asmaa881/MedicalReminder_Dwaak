package com.example.mediacalremider.drugs.add_drugs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mediacalremider.R;

public class AddDrugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);

        NavHostFragment navHostFragment= (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.addDrugContainer);
        NavController navController= navHostFragment.getNavController();
        NavGraph navGraph=navHostFragment.getNavController().getNavInflater().inflate(R.navigation.add_drug_navgation_graph);
        navGraph.setStartDestination(R.id.add_drug_first_fragment);
        navController.setGraph(navGraph);
    }
}