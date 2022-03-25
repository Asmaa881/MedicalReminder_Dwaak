package com.example.mediacalremider.drugs.add_drugs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.mediacalremider.R;

import java.util.ArrayList;

public class AddDrugThirdFragment extends Fragment {

    TextView txtSelectDays;
    EditText editTextDrugNumOfTimes;
    Button btnNextAddDrugFragment4;
    boolean [] selectedDay;
    ArrayList<Integer> dayList = new ArrayList<>();
    String [] dayArray = {"Saturday","Sunday","Monday","Tuseday","Wednesday","Thursday","Friday"};
    public AddDrugThirdFragment() {
        // Required empty public constructor
    }
    public static AddDrugThirdFragment newInstance(String param1, String param2) {
        AddDrugThirdFragment fragment = new AddDrugThirdFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.fragment_add_drug_third, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtSelectDays = view.findViewById(R.id.txtSelectDays);
        editTextDrugNumOfTimes = view.findViewById(R.id.editTextDrugNumOfTimes);
        btnNextAddDrugFragment4 = view.findViewById(R.id.btnNextAddDrugFragment4);
        /*
        int size = Integer.valueOf(editTextDrugNumOfTimes.getText().toString());
        selectedDay = new boolean[size];
        txtSelectDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Days");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            dayList.add(i);
                            Collections.sort(dayList);
                        }
                        else{
                            dayList.remove(i);
                        }
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j=0;j<dayList.size();j++){
                            stringBuilder.append(dayArray[dayList.get(j)]);
                            if(j != dayList.size()-1){
                                stringBuilder.append("& ");
                            }
                        }
                        txtSelectDays.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int k=0;k<selectedDay.length;k++){
                            selectedDay[k] = false;
                            dayList.clear();
                            txtSelectDays.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
        */
        btnNextAddDrugFragment4.setOnClickListener(btnView -> {
            NavController navController= Navigation.findNavController(btnView);
            NavDirections navDirections = AddDrugThirdFragmentDirections.goToAddDrugFourthFragment();
            navController.navigate(navDirections);
        });
    }
}