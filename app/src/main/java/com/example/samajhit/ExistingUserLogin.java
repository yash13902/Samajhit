package com.example.samajhit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ExistingUserLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user_login);

        mAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.emailLogIn);
        EditText pass = findViewById(R.id.passwordLogIn);

        Button login = findViewById(R.id.login);

        TextView signUpView = findViewById(R.id.signUpView);
        signUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingUserLogin.this, NewUserLogin.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passWord = pass.getText().toString();

                if(email.getText().toString().isEmpty()) {
                    email.setError("Email Required");
                    email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Valid Email Required");
                    email.requestFocus();
                    return;
                }
                if(pass.getText().toString().isEmpty()) {
                    pass.setError("Password Required");
                    pass.requestFocus();
                    return;
                }
                if(pass.getText().toString().length()<6) {
                    pass.setError("Min 6 char required");
                    pass.requestFocus();
                    return;
                }

                loginUser(emailText,passWord);
            }
        });
    }

    public void loginUser(String email, String password)
    {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {
                        //Log In success
                        Intent intent = new Intent(ExistingUserLogin.this, Help.class);
                        intent.putExtra("Email",email);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(this, "Invalid Email / Password", Toast.LENGTH_SHORT).show();
                });
    }

}