package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.Meal;
import com.example.nutripal.Models.MealEaten;
import com.example.nutripal.Models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealSummary extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    private FirebaseAuth auth;
    private ListView mealSummaryList;

    private void fetchUserData(String userEmail, String mealType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.get_meals_eaten(userEmail, mealType).enqueue(new Callback<List<MealEaten>>() {
            @Override
            public void onResponse(Call<List<MealEaten>> call, Response<List<MealEaten>> response) {
                if (response.isSuccessful()) {
                    List<MealEaten> meals = response.body();
                    MealEaten[] displayList = new MealEaten[meals.size()];
                    for(int i = 0; i < displayList.length; i++)
                        displayList[i] = meals.get(i);
                    ArrayAdapter<MealEaten> adapter = new ArrayAdapter<>(MealSummary.this, R.layout.list_item, R.id.displaytext, displayList);
                    mealSummaryList.setAdapter(adapter);
                } else
                    Log.d("API NON SUCCESSFUL RESPONSE: ", "FAILURE IN ON RESPONSE BLOCK");

            }

            @Override
            public void onFailure(Call<List<MealEaten>> call, Throwable t) {
                Log.d("API FAILED: ", "FAILURE IN ON FAILURE BLOCK");
            }
        });
    }

            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_summary);
        mealSummaryList = findViewById(R.id.mealSummaryList);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        title = findViewById(R.id.meal_summary_text_food_summary);
        backArrow = findViewById(R.id.backArrow);
        Intent intent = getIntent();
        String title_top = intent.getStringExtra("TITLE");
        title.setText(title_top);
        if(currentUser != null)
            fetchUserData(currentUser.getEmail(), title_top);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}