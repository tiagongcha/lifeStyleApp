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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.gongtia.lifestyle.Room.AppDatabase;
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
import android.util.Log;
import android.view.MenuItem;

import java.io.File;

public class HomeActivity extends AppCompatActivity implements MyRVAdapter.OnTransferListener {

    private FragmentTransaction m_fTrans;
    LocationManager locationManager;
    LocationListener locationListener;
    double latitude;
    double longitude;
    private static final String KEY = "AKIAV7GHWBCADY6NTPEZ";
    private static final String SECRET = "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";
    private String sqlPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadModuleView();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        sqlPath = this.getDatabasePath("LifeStyleDB").getAbsolutePath();
        Log.e("sqlpath ", sqlPath);

        uploadWithTransferUtility();
    }

    private void loadModuleView() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        if (!isWideDisplay()) {
            m_fTrans.replace(R.id.main_container, new ModuleListsFragment());
            m_fTrans.commit();

        } else {
//            tablet version:
            m_fTrans.replace(R.id.fl_master_wd, new ModuleListsFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
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
            case 2:
                stepCountButtonHandler();
                break;
        }
    }

    private void stepCountButtonHandler(){
        Intent stepIntent = new Intent(this, StepCounterActivity.class);
        startActivity(stepIntent);
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
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            } else {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        }

        //We have to grab the search term and construct a URI object from it.
        //We'll hardcode WEB's location here
        Uri searchUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + "hike");

        //Create the implicit intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);

        //If there's an activity associated with this intent, launch it
        if (mapIntent.resolveActivity(this.getPackageManager()) != null) {
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

    public void uploadWithTransferUtility() {

        BasicAWSCredentials credentials = new BasicAWSCredentials(KEY, SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "gongtia" + sqlPath,
                        new File(sqlPath));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.e("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });


    }
}