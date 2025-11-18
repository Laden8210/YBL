package com.example.ybl.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ybl.R;
import com.example.ybl.fragments.driver.DriverDashboardFragment;
import com.example.ybl.fragments.driver.DriverScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverHero extends AppCompatActivity {

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

            if (item.getItemId() == R.id.action_dashboard) {
                selectedFragment = new DriverDashboardFragment();
                return loadFragment(selectedFragment);
            }
            if (item.getItemId() == R.id.action_schedule) {
                selectedFragment = new DriverScheduleFragment();
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
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}