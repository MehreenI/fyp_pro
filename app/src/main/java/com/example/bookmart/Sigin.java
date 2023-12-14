package com.example.bookmart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Sigin extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText user_name;
    private EditText etemail;
    private EditText password;
    private EditText phone;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
        firebaseAuth = FirebaseAuth.getInstance();
        user_name = findViewById(R.id.user_name);
        etemail = findViewById(R.id.etemail);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        confirmPassword = findViewById(R.id.confirmPassword);

        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(view -> signUp());

        Button btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        });
    }




    private void signUp() {
        String username = user_name.getText().toString();
        String email = etemail.getText().toString();
        String password = this.password.getText().toString();
        String phonenumber = phone.getText().toString();
        String confirmpassword = confirmPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || confirmpassword.isEmpty()) {
            Toast.makeText(this, "Fields can't be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "Password and Confirm password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Sign-up Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Exception exception = task.getException();
                        Toast.makeText(this, "Error creating user: " + (exception != null ? exception.getMessage() : ""), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}