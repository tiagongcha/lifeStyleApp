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
public interface ProfileDao {
    @Insert
    void insert(ProfileTable profileTable);

    @Delete
    void delete(ProfileTable profileTable);

    @Update
    void update(ProfileTable profileTable);

    @Query("DELETE FROM profile_table")
    void deleteAll();

    @Query("SELECT * from profile_table ORDER BY profiledata ASC")
    LiveData<List<ProfileTable>> getAll();
}
