package com.example.mediacalremider.dependency;

import com.example.mediacalremider.drugs.DrugsAdapter;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;

import java.util.List;

public interface getDrugsDelegation {
    void getDrugsSuccessfully(List<DrugsModel> drugs);
}
