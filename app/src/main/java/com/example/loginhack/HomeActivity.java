package com.example.loginhack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button needHelp = findViewById(R.id.needHelp);
        Button donate = findViewById(R.id.Donate);

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");


        needHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NeedHelpActivity.class);
                intent.putExtra("Email",email);
                startActivity(intent);
            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DonateActivity.class);
                intent.putExtra("Email",email);
                startActivity(intent);
            }
        });
    }
}
