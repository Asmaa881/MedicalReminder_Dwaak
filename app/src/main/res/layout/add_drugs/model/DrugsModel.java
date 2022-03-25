package com.example.mediacalremider.drugs.add_drugs.model;

import java.util.List;

public class DrugsModel {
    String id;
    String name;
    String Type;
    String strength;
    String reasons;
    String numberOfTimes;
    List<String> daysOrMonthsselected;
    List<String> times;
    String startDate;
    String endDate;
    int icon;
    String instructions;
    String state = "default";

    int currentNumOfPills;
    int numOfRefillReminder;
    String refillReminderTime;

    public void DrugModel(){}

    public int getCurrentNumOfPills() {
        return currentNumOfPills;
    }

    public void setCurrentNumOfPills(int currentNumOfPills) {
        this.currentNumOfPills = currentNumOfPills;
    }

    public int getNumOfRefillReminder() {
        return numOfRefillReminder;
    }

    public void setNumOfRefillReminder(int numOfRefillReminder) {
        this.numOfRefillReminder = numOfRefillReminder;
    }

    public String getRefillReminderTime() {
        return refillReminderTime;
    }

    public void setRefillReminderTime(String refillReminderTime) {
        this.refillReminderTime = refillReminderTime;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    public String getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(String numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    public List<String> getDaysOrMonthsselected() {
        return daysOrMonthsselected;
    }

    public void setDaysOrMonthsselected(List<String> daysOrMonthsselected) {
        this.daysOrMonthsselected = daysOrMonthsselected;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
