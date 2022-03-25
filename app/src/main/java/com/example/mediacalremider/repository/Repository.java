package com.example.mediacalremider.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;

import java.util.List;

public class Repository {
    Context context;
    DrugDAO drugDAO;
    List<DrugsModel> drugs;
    Handler handler;
    public Repository(Context context){
        this.context=context;
        DatabaseRepository db=DatabaseRepository.getInstance(context.getApplicationContext());
        drugDAO=db.drugDAO();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                for(DrugsModel drugsModel:drugs){
                    Log.i("mmmm",drugsModel.getName());
                }

            }
        };
    }

    public List<DrugsModel>getDrugs(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                drugs=drugDAO.getDrugsFromDatabase();
                handler.sendEmptyMessage(0);

            }
        }).start();
        return drugs;
    }
    public void deleteDrug(DrugsModel drug){
        new Thread(new Runnable() {
            @Override
            public void run() {
                drugDAO.deleteMovie(drug);
            }
        }).start();
    }
    public void addDrug(DrugsModel drug){
        new Thread(new Runnable() {
            @Override
            public void run() {
                drugDAO.insertDrug(drug);
            }
        }).start();
    }

   public void updateDrug(DrugsModel drug){
        new Thread(new Runnable() {
            @Override
            public void run() {
                drugDAO.updateDrug(drug);
            }
        }).start();
    }
}
