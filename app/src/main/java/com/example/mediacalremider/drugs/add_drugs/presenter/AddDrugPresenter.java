package com.example.mediacalremider.drugs.add_drugs.presenter;

import com.example.mediacalremider.drugs.add_drugs.model.AddDrugRepository;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.drugs.add_drugs.view.IView;

public class AddDrugPresenter implements IAddDrugPresenter {

    AddDrugRepository model;
    IView iView;
    //IModel iModel;

    public AddDrugPresenter(IView iView){
        this.iView = iView;
        model = new AddDrugRepository();

    }

    @Override
    public void onClick(DrugsModel drugsModel) {
        boolean result = model.saveData(new AddDrugRepository.CallBack() {
            @Override
            public void callBack(boolean result) {
                iView.goToHome(result);
            }
        });

    }
}
