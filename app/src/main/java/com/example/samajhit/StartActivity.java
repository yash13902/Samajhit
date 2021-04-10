package com.example.samajhit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {

            Intent intent = new Intent(StartActivity.this, Help.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startActivity(new Intent(StartActivity.this, ExistingUserLogin.class));
    }
}