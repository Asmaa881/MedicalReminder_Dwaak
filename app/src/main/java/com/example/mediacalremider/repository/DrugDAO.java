package com.example.mediacalremider.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;

import java.util.List;
@Dao
public interface DrugDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDrug(DrugsModel drug);
    @Query("SELECT * from drugs")
    List<DrugsModel> getDrugsFromDatabase();
    @Delete
    void deleteMovie(DrugsModel drug);
    @Update
    void updateDrug(DrugsModel drug);
}
