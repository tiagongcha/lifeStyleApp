package com.example.gongtia.lifestyle.activity;

import android.os.Bundle;

import com.example.gongtia.lifestyle.fragment.ProfileCreateFragment;
import com.example.gongtia.lifestyle.R;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ProfileCreateFragment())
                    .commit();
        }
    }
}
