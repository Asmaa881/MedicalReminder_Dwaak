package com.example.mediacalremider.drugs.add_drugs.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "drugs")
public class DrugsModel  implements Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    String id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "type")
    String type;
    @ColumnInfo(name = "Strength")
    String strength;
    @ColumnInfo(name = "reasons")
    String reasons;
    @ColumnInfo(name = "numberOfTimes")
    String numberOfTimes;
    @ColumnInfo(name = "daysOrMonthsselected")
    List<String> daysOrMonthsselected;
    @ColumnInfo(name = "times")
    List<String> times;
    @ColumnInfo(name = "currentNumOfPills")
    int currentNumOfPills = 0;
    @ColumnInfo(name = "numOfRefillReminder")
    int numOfRefillReminder = 0;
    @ColumnInfo(name = "numOfPills")
    int numOfPills =1;
    @ColumnInfo(name = "refillReminderTime")
    String refillReminderTime = "";
    @ColumnInfo(name = "startDate")
    String startDate;
    @ColumnInfo(name = "endDate")
    String endDate;
    @ColumnInfo(name = "icon")
    int icon;
    @ColumnInfo(name = "instructions")
    String instructions= "No Instruction";
    @ColumnInfo(name = "state")
    String state = "default";
    @ColumnInfo(name = "userId")
    String userId;

    public void DrugModel(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getNumOfPills() {
        return numOfPills;
    }

    public void setNumOfPills(int numOfPills) {
        this.numOfPills = numOfPills;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}