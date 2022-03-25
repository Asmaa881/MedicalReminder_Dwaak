package com.example.mediacalremider.drugs.add_drugs;

import java.util.Date;

public class DrugsModel {
    String name;
    String id;
    String strength;
    String reasons;
    int times;
    int duration;
    int numOfPills;
    String Type;
    int icon;
    Date startDate;
    Date endDate;
    String instructions;

    public DrugsModel(){

    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStrength() {
        return strength;
    }
    public void setStrength(String strength) {
        this.strength = strength;
    }
    public String getReasons() {
        return reasons;
    }
    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
    public int getTimes() {
        return times;
    }
    public void setTimes(int times) {
        this.times = times;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getNumOfPills() {
        return numOfPills;
    }
    public void setNumOfPills(int numOfPills) {
        this.numOfPills = numOfPills;
    }
    public String getType() {
        return Type;
    }
    public void setType(String type) {
        Type = type;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
