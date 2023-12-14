package com.example.bookmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    SaleFragment saleFragment;
    AccountFragment accountFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve user details from the intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String userEmail = intent.getStringExtra("userEmail");
        int userCoins = intent.getIntExtra("userCoins", 0); // Default value is 0 if not found

        // Pass user details to the home fragment
        homeFragment = HomeFragment.newInstance(userId, userEmail, userCoins);
        loadFragment(homeFragment);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize your fragments
        saleFragment = new SaleFragment();
        accountFragment = new AccountFragment();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the selected item's ID
                if (itemId == R.id.home) {
                    loadFragment(homeFragment);
                    return true;
                } else if (itemId == R.id.add) {
                    loadFragment(saleFragment);
                    return true;
                } else if (itemId == R.id.profile) {
                    loadFragment(accountFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
