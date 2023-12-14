package com.example.bookmart;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.CoinManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bookmart.GoogleAdMobManager;
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;



    private List<ImageUpload> bookList;

    private final String TAG = "MainActivity";
    Runnable addCoinsCallback;

    private Activity activity;

    private Button allButton, academicButton, generalButton,coin;

    TextView coinTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);
        coin = view.findViewById(R.id.getCoin);

        coinTextView = view.findViewById(R.id.coin);

        // Set a click listener for the items in the RecyclerView
        bookAdapter.setOnItemClickListener(position -> {
            // Handle item click here --> display book details
            ImageUpload clickedItem = bookList.get(position);
            openBookDetailActivity(clickedItem);
        });

        // Buttons
        allButton = view.findViewById(R.id.All);
        academicButton = view.findViewById(R.id.button5);
        generalButton = view.findViewById(R.id.button6);
        coin = view.findViewById(R.id.getCoin);

        GoogleAdMobManager.getInstance().Initialize(getActivity());

    // When the tap to get more coins button is clicked
        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnShowRewardAd clicked");
//                check if ad is available
                if (GoogleAdMobManager.getInstance().IsRewardedAdAvailable()) {
                    // Show the rewarded ad
                    GoogleAdMobManager.getInstance().ShowRewardedAd(getActivity(), addCoinsCallback);
                } else {
                    Log.d(TAG, "The rewarded ad isn't ready yet.");
                }
            }
        });


//        coin.setOnClickListener(v -> showToast("Tap to get More coins"));

        // Set click listeners for the buttons
//        generalButton.setOnClickListener(v -> showToast("General button clicked"));

//        display all academic books
        academicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AcademicBooks.class);
                startActivity(intent);
            }
        });

//        display all general books
        generalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),GeneralsBooks.class);
                startActivity(intent);
            }
        });

        addCoinsCallback = new Runnable() {
            @Override
            public void run() {
                // Increment coins in Firebase
                int coinsToAdd = 10;
                String userId = "dummyuser";
                CoinManager coinManager = AppController.getInstance().getManager(CoinManager.class);

                // Add coins to Firebase
                coinManager.addCoinsToFirebase(userId, coinsToAdd);
                updateCoinTextView();


                // Display a toast or perform any other actions
                Log.d(TAG, "give addCoins(" + coinsToAdd + ")");
                Toast.makeText(activity, "Reward Given: +" + coinsToAdd + " coins", Toast.LENGTH_SHORT).show();
            }
        };



        fetchDataFromFirebase();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Method to update TextView with current coin count
    private void updateCoinTextView() {
        CoinManager coinManager = AppController.getInstance().getManager(CoinManager.class);
        int currentCoins = coinManager.getTotalCoins();

        // Update the TextView with the current coin count
        coinTextView.setText(String.valueOf(currentCoins));
    }

    private void openBookDetailActivity(ImageUpload clickedItem) {
        // Create an intent to open the BookDetailActivity
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        intent.putExtra("bookName", clickedItem.getBookName());
        intent.putExtra("bookPrice", clickedItem.getBookPrice());
        intent.putExtra("imageUrl", clickedItem.getImageUrl());
        intent.putExtra("description", clickedItem.getDescription());
        intent.putExtra("author", clickedItem.getAuthor());
        intent.putExtra("condition", clickedItem.getCondition());
        startActivity(intent);
    }

    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ImageUpload book = snapshot.getValue(ImageUpload.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }

                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}

