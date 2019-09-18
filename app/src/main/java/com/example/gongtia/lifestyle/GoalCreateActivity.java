package com.example.gongtia.lifestyle;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GoalCreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new GoalCreateFragment())
                    .commit();
        }
    }
}
