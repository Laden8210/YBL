package com.example.ybl.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ybl.R;
import com.example.ybl.fragments.ProfileFragment;
import com.example.ybl.fragments.driver.DriverDashboardFragment;
import com.example.ybl.fragments.driver.DriverScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverHero extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_hero);
        BottomNavigationView bnvHero = findViewById(R.id.bnvHero);

        if (savedInstanceState == null) {
            loadFragment(new DriverDashboardFragment());
        }

        bnvHero.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_dashboard) {
                selectedFragment = new DriverDashboardFragment();
                return loadFragment(selectedFragment);
            }
            if (item.getItemId() == R.id.action_schedule) {
                selectedFragment = new DriverScheduleFragment();
                return loadFragment(selectedFragment);
            }
            if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                return loadFragment(selectedFragment);
            }

            return false;

        });
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nfvHero, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}