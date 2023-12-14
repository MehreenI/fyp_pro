package com.example.bookmart;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class Book {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference postDB = db.getReference("post");

    private String bookId;
    private String title;
    private List<String> author;
    private String isbn;
    private int publicationYear;
    private String genre;
    private int pageCount;
    private double price;
    private List<String> keywords;
    private String coverImgUrl;
    private String description;
    private String publisher;
    private String language;

    public static String generateBookId() {
        Random random = new Random();
        StringBuilder randomComponent = new StringBuilder(14);
        String characters = "ABCDEFGHJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 14; i++) {
            randomComponent.append(characters.charAt(random.nextInt(characters.length())));
        }
        String bookId = randomComponent.toString();
        if (bookId.length() > 14) {
            bookId = bookId.substring(0, 14);
        } else if (bookId.length() < 14) {
            int paddingLength = 14 - bookId.length();
            for (int i = 0; i < paddingLength; i++) {
                bookId += "0";
            }
        }
        return bookId;
    }

    public boolean isIdUnique(String bookId) {
        final boolean[] isUnique = {true};
        postDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(bookId)) {
                    isUnique[0] = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return isUnique[0];
    }
}