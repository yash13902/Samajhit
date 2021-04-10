package com.example.samajhit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Button help = findViewById(R.id.button);
        Button WThelp = findViewById(R.id.button2);

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Help.this, NeedHelp.class);
                intent.putExtra("Email",email);
                startActivity(intent);
            }
        });

        WThelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Help.this, NeedHelp.class);
                intent.putExtra("Email",email);
                startActivity(intent);
            }
        });
    }
}