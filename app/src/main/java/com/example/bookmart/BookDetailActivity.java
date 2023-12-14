package com.example.bookmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    TextView bookname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookname = findViewById(R.id.bookDetailName);
        // Retrieve details from the intent
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        String bookPrice = intent.getStringExtra("bookPrice");
        String imageUrl = intent.getStringExtra("imageUrl");


        // Set the details in the layout elements
        ImageView bookDetailImage = findViewById(R.id.bookDetailImage);
        TextView bookDetailName = findViewById(R.id.bookDetailName);
        TextView bookDetailPrice = findViewById(R.id.bookDetailPrice);



        bookname.setText(bookName);

        bookDetailPrice.setText("Rs: " +bookPrice+"/-");

        // Load the image into the ImageView (you can use Picasso or another image loading library)
        Picasso.get().load(imageUrl).into(bookDetailImage);

    }
}