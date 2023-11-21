package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.*;

public class LoginPage extends AppCompatActivity {
    private EditText email, pass;
    private LinearLayout button;
    private FirebaseAuth mAuth;
    private TextView signUpRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.emailEditText);
        pass = findViewById(R.id.passwordEditText);
        button = findViewById(R.id.loginButton);
        signUpRedirect = findViewById(R.id.signUpPrompt);

        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
            String emailInput = String.valueOf(email.getText());
            String passInput = String.valueOf(pass.getText());
            if (TextUtils.isEmpty(emailInput)) {
                Toast.makeText(LoginPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passInput)) {
                Toast.makeText(LoginPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(emailInput, passInput)
                    .addOnCompleteListener(LoginPage.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginPage.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // This block will execute in case of login failure
                        Log.e("LoginPage", "Login failed", e);
                        Toast.makeText(LoginPage.this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }
}