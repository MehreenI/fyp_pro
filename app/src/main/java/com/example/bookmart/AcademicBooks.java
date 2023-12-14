package com.example.bookmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcademicBooks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<ImageUpload> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_books);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList);
        recyclerView.setAdapter(bookAdapter);

        // Set a click listener for the items in the RecyclerView
        bookAdapter.setOnItemClickListener(position -> {
            // Handle item click here
            ImageUpload clickedItem = bookList.get(position);
            openBookDetailActivity(clickedItem);
        });

        fetchAcademicBooks();
    }

    private void openBookDetailActivity(ImageUpload clickedItem) {
        // Implement this method to open the BookDetailActivity
    }

    private void fetchAcademicBooks() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.orderByChild("bookCategory").equalTo(Enums.BookCategory.ACADEMIC.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                        // Handle error appropriately
                    }
                });
    }
}
