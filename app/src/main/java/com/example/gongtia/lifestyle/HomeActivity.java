package com.example.gongtia.lifestyle;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements MyRVAdapter.OnAdapterDataChannel{

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
            m_fTrans.replace(R.id.fl_detail_wd, new GoalFragment());
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
                hikingButtonHandler();
                break;
            case 1:
                weatherButtonHandler();
                break;

        }
    }

    private void hikingButtonHandler() {
        Fragment selectedFragment = new GoalFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                selectedFragment).commit();
    }

    private void weatherButtonHandler() {
        Fragment selectedFragment = new GoalFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                selectedFragment).commit();
    }

    private boolean isWideDisplay() {
        return getResources().getBoolean(R.bool.isTablet);
    }



}