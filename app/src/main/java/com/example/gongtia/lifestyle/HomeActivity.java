package com.example.gongtia.lifestyle;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements MyRVAdapter.OnPositionPasser{

    private FragmentTransaction m_fTrans;

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
            m_fTrans.replace(R.id.fl_detail_wd, new GoalCreateFragment());
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
            }
            return true;
        }
    };

//each module item button handler
    @Override
    public void onAdapterDataPass(int position) {
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
        Fragment hikingFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                hikingFragment).commit();
    }

    private void weatherButtonHandler() {
//        Fragment weatherFragment = new WeatherButtonFragment();
////        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
////                weatherFragment).commit();
        Intent weatherIntent = new Intent(this, WeatherActivity.class);
        startActivity(weatherIntent);
    }

    private boolean isWideDisplay() {
        return getResources().getBoolean(R.bool.isTablet);
    }



}