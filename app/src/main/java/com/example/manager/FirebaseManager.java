package com.example.manager;


import androidx.annotation.NonNull;

import com.example.bookmart.Manager;
import com.example.bookmart.ImageUpload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager extends Manager {

    public DatabaseReference DBPostPath = FirebaseDatabase.getInstance().getReference("post");
    public DatabaseReference DBUserPath = FirebaseDatabase.getInstance().getReference("user").child("dummyuser");

    @Override
    public void Initialize() { }

    public Query onlyFeaturedPosts() {
        return DBPostPath.orderByChild("featured").equalTo(true);
    }

    public Query getPostsFromCountry(String country) {
        return DBPostPath.orderByChild("country").equalTo(country);
    }

    public Query getUsersWithRole(String role) {
        return DBUserPath.orderByChild("role").equalTo(role);
    }

    List<String> favPostIds = new ArrayList<>();
    public List<String> getFavPosts() {
        DBUserPath.child("favPostId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    favPostIds = (List<String>) dataSnapshot.getValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
        return favPostIds;
    }

    public void setAsFavPost(String postId){
        getFavPosts();
        if (favPostIds == null || !favPostIds.contains(postId)) {
            if (favPostIds == null) {
                favPostIds = new ArrayList<>();
            }
            favPostIds.add(postId);
            DBUserPath.child("favPostId").setValue(favPostIds);
        }
    }

    public void removeFromFavPost(String postId){
        getFavPosts();
        if (favPostIds != null) {
            if (favPostIds.contains(postId)) {
                favPostIds.remove(postId);
                DBUserPath.child("favPostId").setValue(favPostIds);
            }
        }
    }

}
