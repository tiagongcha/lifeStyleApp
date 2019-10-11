package com.example.gongtia.lifestyle.ViewModel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private MutableLiveData<WeatherData> jsonData;
    private WeatherRepository mWeatherRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = new WeatherRepository(application);
        jsonData = mWeatherRepository.getData();

    }
    public void setActivity(Activity input){
        mWeatherRepository.setActivity(input);
    }

    public LiveData<WeatherData> getData() {
        return jsonData;
    }
}
