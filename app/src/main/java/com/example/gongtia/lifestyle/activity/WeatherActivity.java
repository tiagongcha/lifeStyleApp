package com.example.gongtia.lifestyle.activity;

import android.os.Bundle;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.fragment.WeatherInfoFragment;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new WeatherInfoFragment())
                    .commit();
        }
    }

}
