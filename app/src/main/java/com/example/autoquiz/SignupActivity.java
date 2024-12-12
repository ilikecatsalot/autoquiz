// SignupActivity.java
package com.example.autoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;

public class SignupActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText etUsername, etPassword;
    Button btnSignup;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        // Set up the "Already have an account? Log in" clickable text
        SpannableString loginText = new SpannableString("Already have an account? Log in");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Navigate to LoginActivity
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        };
        loginText.setSpan(clickableSpan, 25, loginText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogin.setText(loginText);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());

        // Handle signup button click
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    if (!db.checkUser(username)) {
                        if (db.insertUser(username, password)) {
                            Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

