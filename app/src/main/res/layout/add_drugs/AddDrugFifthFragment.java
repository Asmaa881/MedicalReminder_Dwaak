package com.example.mediacalremider.drugs.add_drugs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mediacalremider.R;

public class AddDrugFifthFragment extends Fragment {


    public AddDrugFifthFragment() {
        // Required empty public constructor
    }

    public static AddDrugFifthFragment newInstance(String param1, String param2) {
        AddDrugFifthFragment fragment = new AddDrugFifthFragment();
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
        return inflater.inflate(R.layout.fragment_add_drug_fifth, container, false);
    }
}