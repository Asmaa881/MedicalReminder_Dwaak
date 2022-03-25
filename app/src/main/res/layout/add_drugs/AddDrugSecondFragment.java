package com.example.mediacalremider.drugs.add_drugs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.mediacalremider.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;

public class AddDrugSecondFragment extends Fragment {
    EditText editTextStartDate, editTextEndDate;
    TextView txtStartDate,txtEndDate;
    ChipGroup chipGroup;
    String drugName;
    Button btnNextAddDrugFragment3;

    public AddDrugSecondFragment() { }

    public static AddDrugSecondFragment newInstance(String param1, String param2) {
        AddDrugSecondFragment fragment = new AddDrugSecondFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_drug_second, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chipGroup = view.findViewById(R.id.chipGroupStrength);
       // editTextStartDate = view.findViewById(R.id.editTextStartDate);
       // editTextEndDate = view.findViewById(R.id.editTextEndDate);
        txtStartDate = view.findViewById(R.id.txtStartDate);
        txtEndDate = view.findViewById(R.id.txtEndDate);
        btnNextAddDrugFragment3 = view.findViewById(R.id.btnNextAddDrugFragment3);
        if(getArguments()!=null){
            drugName =getArguments().getString("drugName");
            getChipGroupStrength(drugName);
        }
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePickDialogForStartDate();
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePickDialogForEndDate();
            }
        });
        btnNextAddDrugFragment3.setOnClickListener(btnView -> {
                    NavController navController= Navigation.findNavController(btnView);
                    NavDirections navDirections = AddDrugSecondFragmentDirections.goToAddDrugThirdFragment();
                    navController.navigate(navDirections);
        });
    }
    private void createDatePickDialogForStartDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                txtStartDate.setText(date);
            }
        },year,month,day );
        datePickerDialog.show();
    }
    private void createDatePickDialogForEndDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                txtEndDate.setText(date);
            }
        },year,month,day );
        datePickerDialog.show();
    }

    private void getChipGroupStrength(String drugType){
        Chip chip1 = new Chip(getContext());
        Chip chip2 = new Chip(getContext());
        Chip chip3 = new Chip(getContext());
        Chip chip4 = new Chip(getContext());
        Chip chip5 = new Chip(getContext());
        Chip chip6 = new Chip(getContext());
        Chip chip7 = new Chip(getContext());
        Chip chip8 = new Chip(getContext());
        Chip chip9 = new Chip(getContext());
        ChipDrawable drawable = ChipDrawable.createFromAttributes(getContext(),null,0, R.style.Widget_MaterialComponents_Chip_Choice);
        switch (drugType){
            case"Pill":
            default:
                chipGroup.removeAllViews();
                chip1.setChipDrawable(drawable);
                chip1.setText("g");
                chip2.setChipDrawable(drawable);
                chip2.setText("IU");
                chip3.setChipDrawable(drawable);
                chip3.setText("mcg");
                chip4.setChipDrawable(drawable);
                chip4.setText("mEq");
                chip5.setChipDrawable(drawable);
                chip5.setText("mg");
                chipGroup.addView(chip1);
                chipGroup.addView(chip2);
                chipGroup.addView(chip3);
                chipGroup.addView(chip4);
                //chipGroup.addView(chip5);
                break;
            case"Solution":
            case"Other":
                chipGroup.removeAllViews();
                chip1.setChipDrawable(drawable);
                chip1.setText("g");
                chip2.setChipDrawable(drawable);
                chip2.setText("IU");
                chip3.setChipDrawable(drawable);
                chip3.setText("mcg");
                chip4.setChipDrawable(drawable);
                chip4.setText("mcg/ml");
                chip5.setChipDrawable(drawable);
                chip5.setText("mEq");
                chip6.setChipDrawable(drawable);
                chip6.setText("mg");
                chip7.setChipDrawable(drawable);
                chip7.setText("mg/ml");
                chip8.setChipDrawable(drawable);
                chip8.setText("ml");
                chip9.setChipDrawable(drawable);
                chip9.setText("%");
                chipGroup.addView(chip1);
                chipGroup.addView(chip2);
                chipGroup.addView(chip3);
                chipGroup.addView(chip4);
                chipGroup.addView(chip5);
                chipGroup.addView(chip6);
                chipGroup.addView(chip7);
                chipGroup.addView(chip8);
                // chipGroup.addView(chip9);
                break;
            case"Injection":
            case"Drops":
                chipGroup.removeAllViews();
                chip1.setChipDrawable(drawable);
                chip1.setText("IU");
                chip2.setChipDrawable(drawable);
                chip2.setText("mcg");
                chip3.setChipDrawable(drawable);
                chip3.setText("mcg/ml");
                chip4.setChipDrawable(drawable);
                chip4.setText("mEq");
                chip5.setChipDrawable(drawable);
                chip5.setText("mg");
                chip6.setChipDrawable(drawable);
                chip6.setText("mg/ml");
                chip7.setChipDrawable(drawable);
                chip7.setText("ml");
                chip8.setChipDrawable(drawable);
                chip8.setText("%");
                chipGroup.addView(chip1);
                chipGroup.addView(chip2);
                chipGroup.addView(chip3);
                chipGroup.addView(chip4);
                chipGroup.addView(chip5);
                chipGroup.addView(chip6);
                chipGroup.addView(chip7);
                //chipGroup.addView(chip8);
                break;
            case"Powder":
                chipGroup.removeAllViews();
                chip1.setChipDrawable(drawable);
                chip1.setText("g");
                chip2.setChipDrawable(drawable);
                chip2.setText("mcg");
                chip3.setChipDrawable(drawable);
                chip3.setText("mg");
                chip4.setChipDrawable(drawable);
                chip4.setText("%");
                chipGroup.addView(chip1);
                chipGroup.addView(chip2);
                chipGroup.addView(chip3);
                break;
            case"Inhaler":
                chipGroup.removeAllViews();
                chip1.setChipDrawable(drawable);
                chip1.setText("mcg");
                chip2.setChipDrawable(drawable);
                chip2.setText("mg");
                chip3.setChipDrawable(drawable);
                chip3.setText("mg/ml");
                chip4.setChipDrawable(drawable);
                chip4.setText("mEq");
                chipGroup.addView(chip1);
                chipGroup.addView(chip2);
                chipGroup.addView(chip3);
                break;
        }
    }

}