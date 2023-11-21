package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterPage extends AppCompatActivity {
    private EditText email, pass, username;
    private LinearLayout button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        username = findViewById(R.id.userNameEditText);
        email = findViewById(R.id.emailEditText);
        pass = findViewById(R.id.passwordEditText);
        button = findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterPage.this, MainActivity.class);
            String emailInput = String.valueOf(email.getText());
            String passInput = String.valueOf(pass.getText());
            if (TextUtils.isEmpty(emailInput)) {
                Toast.makeText(RegisterPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passInput)) {
                Toast.makeText(RegisterPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.createUserWithEmailAndPassword(emailInput, passInput)
                    .addOnCompleteListener(RegisterPage.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterPage.this, "Account created", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}