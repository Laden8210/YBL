package com.example.ybl.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ybl.R;
import com.example.ybl.fragments.ConductorDashboardFragment;
import com.example.ybl.fragments.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConductorHero extends BaseActivity {

    private MaterialToolbar toolbar;
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_hero);

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
                        return new ConductorDashboardFragment();
                    case 1:
                        return new ProfileFragment();
                    default:
                        return new ConductorDashboardFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 2; // Dashboard and Profile
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
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(1, true);
                return true;
            }
            return false;
        });
    }

    public void refreshDashboard() {
        // Navigate to dashboard and trigger refresh if it's a ConductorDashboardFragment
        viewPager.setCurrentItem(0, true);
    }
}