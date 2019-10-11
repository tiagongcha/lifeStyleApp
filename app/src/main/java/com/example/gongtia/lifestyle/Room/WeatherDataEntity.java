package com.example.gongtia.lifestyle.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class WeatherDataEntity {

    @PrimaryKey(autoGenerate = true)
    public int did;

    @ColumnInfo(name = "cityname")
    public String cityName;

    @ColumnInfo(name = "statename")
    public String stateName;

    @ColumnInfo(name="temperature")
    public String temperature;

    @ColumnInfo(name="minTemperature")
    public String minTemperature;

    @ColumnInfo(name="maxTemperature")
    public String maxTemperature;

    @ColumnInfo(name="description")
    public String description;

    @ColumnInfo(name="snow")
    public String snow;

    @ColumnInfo(name="windSpeed")
    public String windSpeed;

    @ColumnInfo(name="winDirection")
    public String winDirection;

    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="weatherCode")
    public String weatherCode;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }


    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public void setDid(int input) {
        this.did = input;
    }
    public int getDid() {
        return did;
    }
    public void setCityName(String input){
        this.cityName = input;
    }
    public String getCityName() {
        return cityName;
    }
    public void setStateName(String input) {
        this.stateName = input;
    }
    public String getStateName() {
        return stateName;
    }

    public String getSnow() {
        return snow;
    }

    public void setSnow(String snow) {
        this.snow = snow;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWinDirection() {
        return winDirection;
    }

    public void setWinDirection(String winDirection) {
        this.winDirection = winDirection;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }
}
