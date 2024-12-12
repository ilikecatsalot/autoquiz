package com.example.autoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.method.LinkMovementMethod;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText etUsername, etPassword;
    Button btnLogin;   // Corrected button reference
    TextView tvSignup; // Corrected TextView reference for "Don't have an account? Sign up"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin); // Adjusted button for login
        tvSignup = findViewById(R.id.tvSignup); // TextView for "Sign up" link

        // Set up the "Don't have an account? Sign up" clickable text
        SpannableString signupText = new SpannableString("Don't have an account? Sign up");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Navigate to SignupActivity
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        };
        signupText.setSpan(clickableSpan, 23, signupText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignup.setText(signupText); // Set clickable text in TextView
        tvSignup.setMovementMethod(LinkMovementMethod.getInstance());

        // Handle login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.checkUserCredentials(username, password)) {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Redirect to HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();  // Optionally call finish() to prevent returning to LoginActivity when back is pressed
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
