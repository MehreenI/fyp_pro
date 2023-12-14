package com.example.bookmart;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmart.ImageUpload;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.bookmart.R;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<ImageUpload> bookList;
    private OnItemClickListener listener;

    public BookAdapter(List<ImageUpload> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_list, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        ImageUpload book = bookList.get(position);

        // Load the book details into the views
        holder.bookName.setText(book.getBookName());
        holder.bookPrice.setText("Price: " + book.getBookPrice() + "/-");

//        3rd party library hai. image k urls ko load karne k liye istemal hoti hai
        Picasso.get().load(book.getImageUrl()).into(holder.bookImage);

        // Set the date in the datetime TextView
        holder.dateTime.setText(book.getUploadDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                        // Open the BookDetailActivity and pass the details
                        Intent intent = new Intent(view.getContext(), BookDetailActivity.class);
                        intent.putExtra("bookName", book.getBookName());
                        intent.putExtra("bookPrice", book.getBookPrice());
                        intent.putExtra("imageUrl", book.getImageUrl());
                        view.getContext().startActivity(intent);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImage;
        TextView bookName;
        TextView bookPrice;
        TextView dateTime;
        public BookViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.imageView);
            bookName = itemView.findViewById(R.id.bookname);
            bookPrice = itemView.findViewById(R.id.price);
            dateTime = itemView.findViewById(R.id.datetime);
        }
    }
}
