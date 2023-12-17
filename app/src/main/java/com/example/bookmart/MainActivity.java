package com.example.bookmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookmart.databinding.ActivityMainBinding;
import com.example.manager.FirebaseManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    //region Attributes
    //region Class Constants
    private ActivityMainBinding actBinding;
    private Activity activity;
    private final String TAG = "MainActivity";
    private FirebaseManager firebaseManager;
    //endregion Class Constants

    HomeFragment homeFragment;
    SaleFragment saleFragment;
    AccountFragment accountFragment;
    //endregion Attributes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());
        activity = this;
        AppController.getInstance().setCurrentActivity(activity);


        // Retrieve user details from the intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        String userEmail = intent.getStringExtra("userEmail");
        int userCoins = intent.getIntExtra("userCoins", 0); // Default value is 0 if not found

        // Pass user details to the home fragment
        homeFragment = HomeFragment.newInstance(userId, userEmail, userCoins);
        loadFragment(homeFragment);


        // Initialize your fragments
        saleFragment = new SaleFragment();
        accountFragment = new AccountFragment();

        actBinding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
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
