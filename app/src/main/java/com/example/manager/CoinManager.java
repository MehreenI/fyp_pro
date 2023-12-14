package com.example.manager;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bookmart.Manager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CoinManager extends Manager{

    //region Attributes
    private final String TAG = "CoinManager";
    private int totalCoins;
    private DatabaseReference databaseReference;
    //endregion Attributes

    //region Singleton
    //endregion Singleton


    public CoinManager() {
    }
    public CoinManager(String userId) {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("totalCoins");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("dummyuser").child("totalCoins");
        Initialize();
    }

    @Override
    public void Initialize() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    totalCoins = dataSnapshot.getValue(Integer.class);
                } else {
                    totalCoins = 0;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        setInitialized(true);
    }

    public int getTotalCoins()
    {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
        databaseReference.setValue(totalCoins);
    }

    public void addCoins(int amount) {
        Log.d(TAG, "addCoins: 5 coins");
        totalCoins += amount;
        setTotalCoins(totalCoins);
    }

    public void deductCoins(int amount) {
        if (totalCoins >= amount) {
            totalCoins -= amount;
            setTotalCoins(totalCoins);
        } else {
            // Handle the case where the user doesn't have enough coins.
        }
    }
    public void addCoinsToFirebase(String userId, int coinsToAdd) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userRef.child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int currentCoins = dataSnapshot.getValue(Integer.class);
                    int newCoins = currentCoins + coinsToAdd;

                    // Update the coins in the database
                    userRef.child("coin").setValue(newCoins);
                } else {
                    // Handle the case where coins data is not available
                    Log.e("Firebase", "User coins data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
                Log.e("Firebase", "Error updating user coins: " + databaseError.getMessage());
            }
        });
    }




}

