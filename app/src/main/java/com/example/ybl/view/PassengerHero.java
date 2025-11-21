package com.example.ybl.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ybl.R;
import com.example.ybl.fragments.BusTrackingFragment;
import com.example.ybl.fragments.MyRequestsFragment;
import com.example.ybl.fragments.RoutesFragment;
import com.example.ybl.fragments.SchedulesFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class PassengerHero extends BaseActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_hero);
        
        initViews();
        setupToolbar();
        setupViewPager();
        setupBottomNavigation();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        toolbar = findViewById(R.id.toolbar);
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
                        return new RoutesFragment();
                    case 1:
                        return new SchedulesFragment();
                    case 2:
                        return new com.example.ybl.fragments.ProfileFragment();
                    default:
                        return new RoutesFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 3; // Number of tabs (including Profile)
            }
        };
        
        viewPager.setAdapter(adapter);
        
        // Disable swipe to prevent accidental navigation
        viewPager.setUserInputEnabled(false);
        
        // Listen to page changes to update bottom navigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.nav_routes);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.nav_schedules);
                        break;

                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_routes) {
                viewPager.setCurrentItem(0, true);
                return true;
            } else if (itemId == R.id.nav_schedules) {
                viewPager.setCurrentItem(1, true);
                return true;

            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(2, true);
                return true;
            }
            return false;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Remove menu since Profile is now in bottom navigation
        return false;
    }
}