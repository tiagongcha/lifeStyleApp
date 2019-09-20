package com.example.gongtia.lifestyle.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.gongtia.lifestyle.fragment.MapFragment;
import com.example.gongtia.lifestyle.fragment.ModuleListsFragment;
import com.example.gongtia.lifestyle.MyRVAdapter;
import com.example.gongtia.lifestyle.fragment.ProfileFragment;
import com.example.gongtia.lifestyle.R;
import com.example.gongtia.lifestyle.fragment.GoalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements MyRVAdapter.OnTransferListener {

    private FragmentTransaction m_fTrans;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadModuleView();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void loadModuleView() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        if(!isWideDisplay()){
            m_fTrans.replace(R.id.main_container, new ModuleListsFragment());
            m_fTrans.commit();

        }else{
//            tablet version:
            m_fTrans.replace(R.id.fl_master_wd, new ModuleListsFragment());
            m_fTrans.replace(R.id.fl_detail_wd, new MapFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

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
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            selectedFragment).commit();
                    break;

                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            selectedFragment).commit();
                    break;

                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;

            }
            return true;
        }
    };

//each module item button handler
    @Override
    public void onTransferPosition(int position) {
        switch (position) {
            case 0:
                weatherButtonHandler();
                break;
            case 1:
                hikingButtonHandler();
                break;
        }
    }

    private void hikingButtonHandler() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                },10);
            }else{
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        }

        //We have to grab the search term and construct a URI object from it.
        //We'll hardcode WEB's location here
        Uri searchUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + "hike");

        //Create the implicit intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);

        //If there's an activity associated with this intent, launch it
        if(mapIntent.resolveActivity(this.getPackageManager())!=null){
            startActivity(mapIntent);
        }
    }

    private void weatherButtonHandler() {
        Intent weatherIntent = new Intent(this, WeatherActivity.class);
        startActivity(weatherIntent);
    }

    private boolean isWideDisplay() {
        return getResources().getBoolean(R.bool.isTablet);
    }



}