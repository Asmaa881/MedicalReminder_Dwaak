package com.example.mediacalremider.drugs.add_drugs.presenter;

import com.example.mediacalremider.drugs.add_drugs.model.AddDrugRepository;

public class AddDrugPresenter implements IAddDrugPresenter {

    AddDrugRepository model;

    public AddDrugPresenter(){
        model = new AddDrugRepository();

    }
    @Override
    public void onsendDrugDataToFireBaseClick(DrugsModel drugsModel) {
        model.saveData();
    }
}
