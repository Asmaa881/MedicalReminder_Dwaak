package com.example.mediacalremider.drugs.add_drugs;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.mediacalremider.R;

import java.util.Calendar;

public class AddDrugFourthFragment extends Fragment {
    TextView txtRefillReminderTime, txtSkip;
    int hour, minute;
    Button btnNextAddDrugFragment5;

    public AddDrugFourthFragment() {
        // Required empty public constructor
    }
    public static AddDrugFourthFragment newInstance(String param1, String param2) {
        AddDrugFourthFragment fragment = new AddDrugFourthFragment();
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
        txtRefillReminderTime = view.findViewById(R.id.txtRefillReminderTime);
        txtSkip = view.findViewById(R.id.txtSkip);
        btnNextAddDrugFragment5 = view.findViewById(R.id.btnNextAddDrugFragment5);
        txtRefillReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        hour = hourOfDay;
                        minute = minutes;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,hour,minute);
                        txtRefillReminderTime.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                },12,0,false);
                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });
        btnNextAddDrugFragment5.setOnClickListener(btnView -> {
            NavController navController= Navigation.findNavController(btnView);
            NavDirections navDirections = AddDrugFourthFragmentDirections.goToAddDrugFifthFragment();
            navController.navigate(navDirections);
        });
        txtSkip.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(view1);
            NavDirections navDirections = AddDrugFourthFragmentDirections.goToAddDrugFifthFragment();
            navController.navigate(navDirections);
        });

    }
}