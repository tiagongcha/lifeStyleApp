package com.example.gongtia.lifestyle.Room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="profile_table")
public class ProfileTable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "userName")
    private String userName;

    @NonNull
    @ColumnInfo(name = "profiledata")
    private String profileJson;

    public ProfileTable(@NonNull String userName, @NonNull String profileJson){
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