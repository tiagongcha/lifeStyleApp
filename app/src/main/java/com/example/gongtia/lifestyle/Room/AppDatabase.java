package com.example.gongtia.lifestyle.Room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gongtia.lifestyle.Profile;
import com.example.gongtia.lifestyle.R;

@Database(entities = {WeatherDataEntity.class, ProfileTable.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "LifeStyleDB";

    private static AppDatabase dbInstance;

    public static AppDatabase getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (AppDatabase.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).addCallback(sOnOpenCallback).fallbackToDestructiveMigration().build();
                }
            }

        }

        return dbInstance;
    }

    private static RoomDatabase.Callback sOnOpenCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };


    public abstract WeatherDataDao weatherDataDao();

    public abstract  ProfileDao profileDao();

}
