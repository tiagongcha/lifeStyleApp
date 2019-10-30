package com.example.gongtia.lifestyle.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface StepCounterDao {

    @Insert
    void insertStepCounterTable(StepCounterTable sct) ;

    @Delete
    void deleteStepCounterTable(StepCounterTable sct);

    @Update
    void updateStepCounterTable(StepCounterTable sct);


    @Query("select * from StepCounterTable")
    List<StepCounterData> getAll();

    @Query("delete from StepCounterTable")
    void deleteAll();

}
