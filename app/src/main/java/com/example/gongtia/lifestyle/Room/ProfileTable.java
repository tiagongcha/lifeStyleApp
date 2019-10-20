package com.example.gongtia.lifestyle.Room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="profile_table")
public class ProfileTable {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileJson() {
        return profileJson;
    }

    public void setProfileJson(String profileJson) {
        this.profileJson = profileJson;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    @PrimaryKey(autoGenerate = true)
    public int uID;

    @ColumnInfo(name = "userName")
    public String userName;

    @ColumnInfo(name = "profiledata")
    public String profileJson;

    public ProfileTable(@NonNull String userName, @NonNull String profileJson){
        this.userName = userName;
        this.profileJson = profileJson;
    }


}
