package com.example.gongtia.lifestyle.Room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class StepCounterTable {

    public StepCounterTable() {

    }

    @PrimaryKey(autoGenerate = true)
    public int STid;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "steps")
    public int steps;


    public int getSTid() {
        return STid;
    }

    public void setSTid(int STid) {
        this.STid = STid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
