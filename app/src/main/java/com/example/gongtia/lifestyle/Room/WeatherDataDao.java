package com.example.gongtia.lifestyle.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDataDao {

    @Insert
    void insertWeatherDataEntity(WeatherDataEntity wd);

    @Delete
    void deleteWeatherDataEntity(WeatherDataEntity wd);

    @Update
    void updateWeatherDataEntity(WeatherDataEntity wd);


    @Query("select * from WeatherDataEntity" )
    List<WeatherDataEntity> getAll();

    @Query("delete from WeatherDataEntity")
    void deleteAll();
}
