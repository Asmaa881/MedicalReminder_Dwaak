package com.example.mediacalremider.drugs.add_drugs.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediacalremider.R;

import java.util.ArrayList;
import java.util.List;


public class AddDrugThirdFragment extends Fragment {

    TextView txtSelectDays;
    EditText editTextDrugNumOfTimes;
    Button btnNextAddDrugFragment4;
    RadioGroup radioGroupOptions;
    RadioButton radioPerWeek, radioPerDay;
    RecyclerView recyclerDrugTimes;
    List<String> days = new ArrayList<>();
    boolean [] selectedDay;
    ArrayList<Integer> dayList = new ArrayList<>();
    String [] dayArray;
    List times;

    public AddDrugThirdFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_drug_third, container, false);
        return view;
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtSelectDays = view.findViewById(R.id.txtSelectDays);
        editTextDrugNumOfTimes = view.findViewById(R.id.editTextDrugNumOfTimes);
        btnNextAddDrugFragment4 = view.findViewById(R.id.btnNextAddDrugFragment4);
        radioGroupOptions = view.findViewById(R.id.radioGroupOptions);
        radioPerWeek = view.findViewById(R.id.radioPerWeek);
        radioPerDay = view.findViewById(R.id.radioPerDay);
        recyclerDrugTimes = view.findViewById(R.id.recyclerDrugTimes);
        dayArray = getResources().getStringArray(R.array.dayArray);
        selectedDay = new boolean[dayArray.length];
        editTextDrugNumOfTimes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextDrugNumOfTimes.setTextColor(R.color.teal_700);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        radioPerDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtSelectDays.setVisibility(View.INVISIBLE);
                recyclerDrugTimes.setVisibility(View.VISIBLE);
                setRecyclerViewDrugTimes();
            }
        });

        radioPerWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.selectDays);
                builder.setCancelable(false);
                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int postion, boolean isChecked) {
                        if(isChecked){
                           if(! dayList.contains(postion)){
                                dayList.add(postion);
                            }
                            else{
                                dayList.remove(postion);
                            }
                        }
                        else{
                            dayList.remove(postion);
                        }
                    }
                });
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String item ="";
                        for(int j=0;j<dayList.size();j++){
                            item = item + dayArray[dayList.get(j)];
                            if(j != dayList.size()-1){
                                item = item+ " & ";
                            }
                        }
                        txtSelectDays.setText(item);
                        setRecyclerViewDrugTimes();
                    }
                });
                builder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton(R.string.clearAll, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int k=0;k<selectedDay.length;k++){
                            selectedDay[k] = false;
                            dayList.clear();
                            txtSelectDays.setText("");
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        btnNextAddDrugFragment4.setOnClickListener(btnView -> {
            String numOfTime = editTextDrugNumOfTimes.getText().toString()+ getSelectedOption();
            String option = getSelectedOption();
            if (getActivity() != null) {
                if(editTextDrugNumOfTimes.getText().toString().trim().equalsIgnoreCase("")){
                    editTextDrugNumOfTimes.setError("This field is required");
                }
                else if (option.isEmpty()){
                    Toast.makeText(getContext(), "Please Choose Per which!!!", Toast.LENGTH_SHORT).show();
                }
                else if(ReminderTimesAdapter.timesSelected.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Reminder Times", Toast.LENGTH_SHORT).show();
                }
                else{
                    AddDrugActivity.drugsModel.setNumberOfTimes(numOfTime);
                    AddDrugActivity.drugsModel.setDaysOrMonthsselected(days);
                    AddDrugActivity.drugsModel.setTimes(ReminderTimesAdapter.timesSelected);
                    Log.i("TAG", String.valueOf(ReminderTimesAdapter.timesSelected.get(0)));
                    Log.i("TAG", String.valueOf(ReminderTimesAdapter.timesSelected.get(1)));
                    NavController navController= Navigation.findNavController(btnView);
                    NavDirections navDirections = AddDrugThirdFragmentDirections.goToAddDrugFourthFragment();
                    navController.navigate(navDirections);
                }
            }





        });
    }
    private void setRecyclerViewDrugTimes(){
        int size = Integer.parseInt(editTextDrugNumOfTimes.getText().toString());
        times=new ArrayList();
        recyclerDrugTimes.setHasFixedSize(true);
        for(int timecounter=1; timecounter<=size;timecounter++){
            times.add("Select Time "+ timecounter);
        }

        ReminderTimesAdapter adapter = new ReminderTimesAdapter(getContext(), times);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerDrugTimes.setLayoutManager(layoutManager);
        recyclerDrugTimes.setAdapter(adapter);
    }
    private void setTxtSelectDays(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int j=0;j<dayList.size();j++){
            stringBuilder.append(dayArray[dayList.get(j)]);
            if(j != dayList.size()-1){
                stringBuilder.append("& ");
            }
        }
        txtSelectDays.setText(stringBuilder.toString());
    }

    private String getSelectedOption() {
        String optionSelected = null;
        switch (radioGroupOptions.getCheckedRadioButtonId()) {
            case R.id.radioPerDay:
                optionSelected = " Per Day";
                break;
            case R.id.radioPerWeek:
                optionSelected = " Per Week";
                break;
            case R.id.radioPerMonth:
                optionSelected = " Per Month";
                break;
        }
        return optionSelected;
    }

}