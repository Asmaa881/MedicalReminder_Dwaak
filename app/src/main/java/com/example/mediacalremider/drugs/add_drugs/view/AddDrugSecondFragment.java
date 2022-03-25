package com.example.mediacalremider.drugs.add_drugs.view;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.mediacalremider.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AddDrugSecondFragment extends Fragment {
    EditText editTextDrugStrength;
    TextView txtStartDate,txtEndDate;
    String drugType ,drugStrengthSelected;
    Button btnNextAddDrugFragment3;
    String [] options;
    String date1, date2;
    long diffrence;
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextDrugStrength= view.findViewById(R.id.editTextDrugStrength);
        AutoCompleteTextView autoCompleteTextView1 = view.findViewById(R.id.autoCompleteDrugStrengthsUnit);
        txtStartDate = view.findViewById(R.id.txtStartDate);
        txtEndDate = view.findViewById(R.id.txtEndDate);
        btnNextAddDrugFragment3 = view.findViewById(R.id.btnNextAddDrugFragment3);
        if(getArguments()!= null){
            drugType =getArguments().getString("drugType");
            SetStrengthUnitsMenu(drugType);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.stringth_unit_options, options);
        autoCompleteTextView1.setAdapter(arrayAdapter);
        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drugStrengthSelected = arrayAdapter.getItem(i).toString();
            }
        });

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createDatePickDialogForStartDate();
            }
        });
        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createDatePickDialogForEndDate();
            }
        });

        btnNextAddDrugFragment3.setOnClickListener(btnView -> {
            if (getActivity() != null) {
                String startDate = txtStartDate.getText().toString();
                String endDate = txtEndDate.getText().toString();
                if(editTextDrugStrength.getText().toString().trim().equalsIgnoreCase("")
                        && autoCompleteTextView1.getText().toString().trim().equalsIgnoreCase("")
                        && date1.isEmpty()
                        && date2.isEmpty()){
                    editTextDrugStrength.setError("This field is required");
                    autoCompleteTextView1.setError("This field is required");
                    txtStartDate.setError("Plrase Select Start Date");
                    txtEndDate.setError("Plrase Select End Date");
                }
                else if(editTextDrugStrength.length()==0){
                    editTextDrugStrength.setError("This field is required");
                }
                else if(autoCompleteTextView1.length()==0){
                    autoCompleteTextView1.setError("This field is required");
                }
                else if(date1==null){
                    txtStartDate.setError("Plrase Select End Date");
                }
                else if(date2==null){
                    txtEndDate.setError("Plrase Select Start Date");
                }
                else{
                    String drudStrength = editTextDrugStrength.getText().toString() + " "+ drugStrengthSelected;
                    AddDrugActivity.drugsModel.setStrength(drudStrength);
                    AddDrugActivity.drugsModel.setStartDate(txtStartDate.getText().toString());
                    AddDrugActivity.drugsModel.setEndDate(txtEndDate.getText().toString());
                    Log.i("TAG", txtStartDate.getText().toString());
                    Log.i("TAG",txtEndDate.getText().toString());
                    NavController navController= Navigation.findNavController(btnView);
                    NavDirections navDirections = AddDrugSecondFragmentDirections.goToAddDrugThirdFragment();
                    navController.navigate(navDirections);
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDatePickDialogForStartDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                 date2 = day+"/"+month+"/"+year;
                txtStartDate.setText(date2);
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()-1000);
        datePickerDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createDatePickDialogForEndDate(){
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                 date1 = day+"/"+month+"/"+year;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                try {
                    Date firstDate = sdf.parse(date2);
                    Date secondDate = sdf.parse(date1);

                    long diff = secondDate.getTime() - firstDate.getTime();

                    TimeUnit time = TimeUnit.DAYS;
                     diffrence = time.convert(diff, TimeUnit.MILLISECONDS);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                txtEndDate.setText(date1);
            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()-1000);
        datePickerDialog.show();
    }
    private void SetStrengthUnitsMenu(String drugType){

        switch (drugType) {
            case"Pill":
                options = getResources().getStringArray(R.array.pillStringsUnits);
                break;
            case"Solution":
            case"Other":
                options = getResources().getStringArray(R.array.solutionOROtherStringsUnits);
                break;
            case"Injection":
                options = getResources().getStringArray(R.array.injectionStringsUnits);
                break;
            case"Drops":
                options = getResources().getStringArray(R.array.dropsStringsUnits);
                break;
            case"Powder":
                options = getResources().getStringArray(R.array.powderStringsUnits);
                break;
            case"Inhaler":
                options = getResources().getStringArray(R.array.inhalerStringsUnits);
                break;
        }
    }

}