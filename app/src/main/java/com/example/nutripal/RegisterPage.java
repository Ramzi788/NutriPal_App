package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.User;
import com.google.firebase.auth.FirebaseAuth;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterPage extends AppCompatActivity {
    private EditText email, pass, username;
    private TextView loginPrompt;
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
        loginPrompt = findViewById(R.id.LoginPrompt);

        mAuth = FirebaseAuth.getInstance();
        loginPrompt.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterPage.this, LoginPage.class);
            startActivity(intent);
        });

        button.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterPage.this, EnterDetails.class);
            String emailInput = String.valueOf(email.getText()).toLowerCase();
            String passInput = String.valueOf(pass.getText());
            String usernameInput = String.valueOf(username.getText());
            if (TextUtils.isEmpty(emailInput)) {
                Toast.makeText(RegisterPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passInput)) {
                Toast.makeText(RegisterPage.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getResources().getString(R.string.IP))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FastAPIEndpoint apiService = retrofit.create(FastAPIEndpoint.class);
            User newUser = new User(usernameInput, emailInput, passInput);
            Call<ResponseBody> call = apiService.registerUser(newUser);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        mAuth.signInWithEmailAndPassword(emailInput, passInput)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()) {
                                        startActivity(intent); // Start next activity only if sign in is successful
                                    } else {

                                    }
                                });
                    }
                    else
                        System.out.println("Failed to register via response");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("Failed to register");
                }
            });
            mAuth.signInWithEmailAndPassword(emailInput,passInput);
            startActivity(intent);
        });
    }
}