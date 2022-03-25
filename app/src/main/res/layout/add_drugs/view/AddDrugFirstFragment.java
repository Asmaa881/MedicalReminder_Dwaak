package com.example.mediacalremider.drugs.add_drugs.view;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.mediacalremider.R;

public class AddDrugFirstFragment extends Fragment {
    String drugTypeSelected , drugName, drugTakingFor;
    EditText editTextDrugName, editTextDrugTakingFor;
    public static final String DRUGNAME="drugName";
    public static final String DRUGTYPE="drugType";
    public static final String DRUGTAKINGFOR="drugTakingFor";

    public AddDrugFirstFragment() {
    }
    public static AddDrugFirstFragment newInstance(String param1, String param2) {
        AddDrugFirstFragment fragment = new AddDrugFirstFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_drug_first, container, false);
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnGoToSecondAddDrugFragment = view.findViewById(R.id.btnNextAddDrugFragment2);
        editTextDrugName = view.findViewById(R.id.editTextDrugName);
        editTextDrugTakingFor = view.findViewById(R.id.editTextDrugTakingFor);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteDrugType);
        String [] options = getResources().getStringArray(R.array.drugTypeOptions);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), R.layout.drug_type_option, options);
        //autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drugTypeSelected = arrayAdapter.getItem(i).toString();
            }
        });

        btnGoToSecondAddDrugFragment.setOnClickListener(btnView -> {
            if (getActivity() != null) {
                drugName= editTextDrugName.getText().toString();
                drugTakingFor = editTextDrugTakingFor.getText().toString();
                if(drugName.trim().equalsIgnoreCase("")
                        && drugTakingFor.trim().equalsIgnoreCase("")
                        && autoCompleteTextView.getText().toString().trim().equalsIgnoreCase("")){
                    editTextDrugName.setError("This field is required");
                    editTextDrugTakingFor.setError("This field is required");
                    autoCompleteTextView.setError("This field is required");
                }
                else if(drugName.trim().equalsIgnoreCase("")){
                    editTextDrugName.setError("This field is required");
                }
                else if(drugTakingFor.trim().equalsIgnoreCase("")){
                    editTextDrugTakingFor.setError("This field is required");
                }
                // autoCompleteTextView.length()==0
                else if(autoCompleteTextView.getText().toString().trim().equalsIgnoreCase("")){
                    autoCompleteTextView.setError("This field is required");
                }
                else{
                    NavController navController= Navigation.findNavController(btnView);
                    AddDrugActivity.drugsModel.setName(editTextDrugName.getText().toString());
                    AddDrugActivity.drugsModel.setReasons(editTextDrugTakingFor.getText().toString());
                    AddDrugActivity.drugsModel.setType(drugTypeSelected);
                    Bundle bundle=new Bundle();
                    bundle.putString(DRUGNAME,editTextDrugName.getText().toString());
                    bundle.putString(DRUGTAKINGFOR,editTextDrugTakingFor.getText().toString());
                    bundle.putString(DRUGTYPE,drugTypeSelected);
                    navController.navigate(R.id.add_drug_second_fragment, bundle);
                }
            }
        });
    }



}
