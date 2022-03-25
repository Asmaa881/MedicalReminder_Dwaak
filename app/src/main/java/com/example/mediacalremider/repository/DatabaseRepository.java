package com.example.mediacalremider.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;

@Database(entities = { DrugsModel.class},version = 1,exportSchema = false)
@TypeConverters(TypeConverter.class)
public abstract class DatabaseRepository extends RoomDatabase {
    static DatabaseRepository databaseRepository=null;
    public abstract DrugDAO drugDAO();
    public static synchronized DatabaseRepository getInstance(Context context){
        if(databaseRepository==null){
            databaseRepository= Room.databaseBuilder(context.getApplicationContext(),DatabaseRepository.class,"drugs").build();
        }
        return databaseRepository;
    }

}
