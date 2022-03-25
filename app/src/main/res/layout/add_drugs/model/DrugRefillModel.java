package com.example.mediacalremider.drugs.add_drugs.model;

public class DrugRefillModel {
    int currentNumOfPills;
    int numOfRefillReminder;
    String refillReminderTime;

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
}
