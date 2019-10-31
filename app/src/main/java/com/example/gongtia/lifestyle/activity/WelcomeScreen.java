package com.example.gongtia.lifestyle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.Room.AppDatabase;
import com.example.gongtia.lifestyle.Room.WeatherData;
import com.example.gongtia.lifestyle.repository.WeatherRepository;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;
    public static AppDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ImageView img= (ImageView) findViewById(R.id.imageView);
        img.setImageResource(R.drawable.heart);

        db = AppDatabase.getInstance(getBaseContext());

        WeatherData wd = new WeatherData();
        wd.cityName = "Salt Lake City";

        WeatherRepository.saveDataToDB(wd);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
