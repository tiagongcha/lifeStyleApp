package com.example.gongtia.lifestyle;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ProfileEditFragment())
                    .commit();
        }
    }
}
