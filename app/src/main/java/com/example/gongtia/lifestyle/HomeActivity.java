package com.example.gongtia.lifestyle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
         {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case 10:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    configureButton();
//                return;
//        }
//    }
//
//    private void configureButton() {
//
//        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
//    }

         private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                 Fragment selectedFragment = null;

                 switch (menuItem.getItemId()){
                     case R.id.nav_Home:
                         Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                         startActivity(homeIntent);
                         break;

                     case R.id.nav_goal:
                         selectedFragment = new GoalFragment();
                         break;

                     case R.id.nav_profile:
                         selectedFragment = new ProfileFragment();
                         break;
                 }
                 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                         selectedFragment).commit();
                 return true;
             }
         };
}
