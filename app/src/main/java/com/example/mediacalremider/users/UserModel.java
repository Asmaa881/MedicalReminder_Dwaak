package com.example.mediacalremider.users;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    String id;
    String name;
    String email;
    String requestState;
    String password;
    List<String> dependency=new ArrayList<>();
    List<String> myHealthTacker=new ArrayList<>();
    List<String>drugsIds=new ArrayList<>();
    String DrugState="Taken";

    public UserModel() {
    }

    public String getDrugState() {
        return DrugState;
    }
    public void setDrugState(String drugState) {
        DrugState = drugState;
    }
    public List<String> getDrugsIds() {
        return drugsIds;
    }

    public void setDrugsIds(List<String> drugsIds) {
        this.drugsIds = drugsIds;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    int icon;


    public List<String> getMyHealthTacker() {
        return myHealthTacker;
    }

    public void setMyHealthTacker(List<String> myHealthTacker) {
        this.myHealthTacker = myHealthTacker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getDependency() {
        return dependency;
    }

    public void setDependency(List<String> dependency) {
        this.dependency = dependency;
    }
}
