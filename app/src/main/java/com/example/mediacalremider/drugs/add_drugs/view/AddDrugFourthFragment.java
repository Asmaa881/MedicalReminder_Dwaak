package com.example.mediacalremider.drugs.add_drugs.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.mediacalremider.R;
import com.example.mediacalremider.drugs.add_drugs.presenter.AddDrugPresenter;
import com.example.mediacalremider.home.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddDrugFourthFragment extends Fragment implements IView{
    EditText editTextInstructions;
    Button btnSaveOrNextAddedDrug;
    ImageView  iconPill, iconInjection, iconInhaler,iconDrops, iconPowder  ,iconTemp ,iconOther;
    private String saveCurrentDate, saveCurrentTime, drugRandomKey;
    private AddDrugPresenter presenter;

    public AddDrugFourthFragment() {
    }
    public static AddDrugFourthFragment newInstance(String param1, String param2) {
        AddDrugFourthFragment fragment = new AddDrugFourthFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_drug_fourth, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new AddDrugPresenter(this);
        editTextInstructions = view.findViewById(R.id.editTextInstructions);
        btnSaveOrNextAddedDrug = view.findViewById(R.id.btnSaveOrNextAddedDrug);
        iconDrops = view.findViewById(R.id.iconDrops);
        iconOther = view.findViewById(R.id.iconOther);
        iconTemp = view.findViewById(R.id.iconTemp);
        iconInhaler = view.findViewById(R.id.iconInhaler);
        iconInjection = view.findViewById(R.id.iconInjection);
        iconPill = view.findViewById(R.id.iconPill);
        iconPowder = view.findViewById(R.id.iconPowder);
        String drugType = AddDrugActivity.drugsModel.getType();

        iconPowder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.powder);
                iconPowder.setAlpha(0.5f);
            }
        });
        iconTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.temp);
                iconTemp.setAlpha(0.5f);
            }
        });
        iconDrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.drops);
                iconDrops.setAlpha(0.5f);
            }
        });
        iconPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.pill);
                iconPill.setAlpha(0.5f);
            }
        });
        iconInjection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.injection);
                iconInjection.setAlpha(0.5f);
            }
        });
        iconInhaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.inhaler);
                iconInhaler.setAlpha(0.5f);
            }
        });
        iconOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDrugActivity.drugsModel.setIcon(R.drawable.other);
                iconOther.setAlpha(0.5f);
            }
        });

        if(drugType.equals("Pill")){
            btnSaveOrNextAddedDrug.setText("Next");
            btnSaveOrNextAddedDrug.setOnClickListener(btnView -> {
                AddDrugActivity.drugsModel.setInstructions(editTextInstructions.getText().toString());
                //AddDrugActivity.drugsModel.setIcon(iconSeected);
                NavController navController= Navigation.findNavController(btnView);
                NavDirections navDirections = AddDrugFourthFragmentDirections.goToAddDrugFifthFragment();
                navController.navigate(navDirections);
            });
        }
        else{
            drugRandomKey = randomkey();
            btnSaveOrNextAddedDrug.setText("Save");
            btnSaveOrNextAddedDrug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // AddDrugActivity.drugsModel.setId(drugRandomKey);
                    AddDrugActivity.drugsModel.setInstructions(editTextInstructions.getText().toString());
                    //AddDrugActivity.drugsModel.setIcon(iconSeected);
                    presenter.onClick(AddDrugActivity.drugsModel);
                }
            });
        }
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


    public boolean connection(){
        boolean checkNetwork = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if(activeNetwork!=null){
            checkNetwork = true;
        }
        return  checkNetwork;
    }

    @Override
    public void goToHome(boolean result) {
        Log.i("connectionnnn", "connection: "+ connection());
        if(connection()) {
            if (result == true) {
                Toast.makeText(getContext(), "Data saved.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Data not saved.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "You are offline.", Toast.LENGTH_SHORT).show();
            Log.i("connectionnnn", "You are offline.");
        }
    }
}