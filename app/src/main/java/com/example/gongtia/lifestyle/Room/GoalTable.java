package com.example.gongtia.lifestyle.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName="goal_table")
public class GoalTable {

    @PrimaryKey(autoGenerate = true)
    public int uID;

    @ColumnInfo(name = "userName")
    public String userName;


    //    store json string in value col
    @NonNull
    @ColumnInfo(name = "profiledata")
    private String profileJson;

    public GoalTable(@NonNull String userName, @NonNull String profileJson){
        this.userName = userName;
        this.profileJson = profileJson;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return userName;
    }

    public String getProfileJson(){
        return profileJson;
    }
}

