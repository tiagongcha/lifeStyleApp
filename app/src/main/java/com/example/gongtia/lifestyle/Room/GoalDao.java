package com.example.gongtia.lifestyle.Room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GoalTable goalTable);

    @Delete
    void delete(GoalTable goalTable);

    @Update
    void update(GoalTable goalTable);

    @Query("DELETE FROM goal_table")
    void deleteAll();

    @Query("SELECT * from goal_table")
    LiveData<List<ProfileTable>> getAll();
}