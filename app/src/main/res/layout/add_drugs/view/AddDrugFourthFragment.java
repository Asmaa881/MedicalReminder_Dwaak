package com.example.mediacalremider.drugs.add_drugs.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.Calendar;

public class AddDrugFourthFragment extends Fragment {
    EditText editTextInstructions;
    RadioGroup radioGroup;
    Button btnSaveOrNextAddedDrug;
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
        presenter = new AddDrugPresenter();
        radioGroup= view.findViewById(R.id.radioGroup);
        editTextInstructions = view.findViewById(R.id.editTextInstructions);
        btnSaveOrNextAddedDrug = view.findViewById(R.id.btnSaveOrNextAddedDrug);
        String drugType = AddDrugActivity.drugsModel.getType();
        int iconSeected = getSelectedIcon();
        if(drugType.equals("Pill")){
            btnSaveOrNextAddedDrug.setText("Next");
            btnSaveOrNextAddedDrug.setOnClickListener(btnView -> {
                AddDrugActivity.drugsModel.setInstructions(editTextInstructions.getText().toString());
                AddDrugActivity.drugsModel.setIcon(iconSeected);
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
                    AddDrugActivity.drugsModel.setId(drugRandomKey);
                    presenter.onsendDrugDataToFireBaseClick(AddDrugActivity.drugsModel);
                    //saveDrugDate();
                    Toast.makeText(getContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private int getSelectedIcon() {
        int imageIcon;
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radioPill:
                imageIcon= R.drawable.pill;
                break;
            case R.id.radioDrops:
                imageIcon= R.drawable.drops;
                break;
            case R.id.radioInhaler:
                imageIcon= R.drawable.inhaler;
                break;
            case R.id.radioInjection:
                imageIcon= R.drawable.injection;
                break;
            case R.id.radioPowder:
                imageIcon= R.drawable.powder;
                break;
            case R.id.radioTemp:
                imageIcon= R.drawable.temp;
                break;
            case R.id.radioOther:
                imageIcon = R.drawable.other;
            default:
                imageIcon= R.drawable.other;
                break;
        }
        return imageIcon;
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