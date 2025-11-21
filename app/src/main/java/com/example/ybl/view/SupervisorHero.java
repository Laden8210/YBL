package com.example.ybl.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ybl.R;
import com.example.ybl.fragments.ProfileFragment;
import com.example.ybl.fragments.SupervisorBusesFragment;
import com.example.ybl.fragments.SupervisorDashboardFragment;
import com.example.ybl.fragments.SupervisorMapFragment;
import com.example.ybl.fragments.SupervisorTripsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SupervisorHero extends BaseActivity {

    private MaterialToolbar toolbar;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_hero);

        initViews();
        setupToolbar();
        setupViewPager();
        setupBottomNavigation();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupViewPager() {
        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new SupervisorDashboardFragment();
                    case 1:
                        return new SupervisorBusesFragment();
                    case 2:
                        return new SupervisorTripsFragment();

                    case 3:
                        return new ProfileFragment();
                    default:
                        return new SupervisorDashboardFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 4; // Dashboard, Buses, Trips, Map, Profile
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);

        // Listen to page changes to update bottom navigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.nav_buses);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.nav_trips);
                        break;
                    case 3:
                        bottomNavigation.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_buses) {
                viewPager.setCurrentItem(1, true);
                return true;
            } else if (itemId == R.id.nav_trips) {
                viewPager.setCurrentItem(2, true);
                return true;

            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(4, true);
                return true;
            }
            return false;
        });
    }
}