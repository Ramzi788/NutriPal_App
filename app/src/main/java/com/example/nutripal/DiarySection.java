package com.example.nutripal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.MealAdapter;
import com.example.nutripal.Models.MealEaten;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiarySection extends Fragment {

    public DiarySection() {}

    private TextView bkfst_calories,lunch_calories, dinner_calories,snack_calories;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_meal, container, false);
        LinearLayout box =view.findViewById(R.id.add_meal);
        LinearLayout bkfst_box = view.findViewById(R.id.breakfast_box);
        LinearLayout lunch_box = view.findViewById(R.id.lunch_box);
        LinearLayout dinner_box = view.findViewById(R.id.dinner_box);
        LinearLayout snack_box = view.findViewById(R.id.snacks_box);
        bkfst_calories = view.findViewById(R.id.breakfast_calories);
        lunch_calories = view.findViewById(R.id.Lunch_calories);
        dinner_calories = view.findViewById(R.id.Dinner_calories);
        snack_calories = view.findViewById(R.id.Snack_calories);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null) {
            fetchUserData(currentUser.getEmail(), "Breakfast", bkfst_calories);
            fetchUserData(currentUser.getEmail(), "Lunch", lunch_calories);
            fetchUserData(currentUser.getEmail(), "Dinner", dinner_calories);
            fetchUserData(currentUser.getEmail(), "Snack", snack_calories);
        }
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMeal.class);
                startActivity(intent);
            }
        });

        bkfst_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.breakfas);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        lunch_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.lunch);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        dinner_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.Dinner);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        snack_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.Snack);
                intent.putExtra("TITLE","Snack");
                startActivity(intent);
            }
        });
        return view;
    }
    private void fetchUserData(String userEmail, String mealType, TextView caloriesTextView) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.get_meals_eaten(userEmail, mealType).enqueue(new Callback<List<MealEaten>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<MealEaten>> call, Response<List<MealEaten>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MealEaten> meals = response.body();
                    int calorieSum = 0;
                    for (MealEaten meal : meals) {
                        calorieSum += meal.getCalories();
                    }
                        caloriesTextView.setText("Calories: " + calorieSum);

                } else {
                    Log.d("API NON SUCCESSFUL RESPONSE", "Failure in onResponse");
                }
            }

            @Override
            public void onFailure(Call<List<MealEaten>> call, Throwable t) {
                Log.d("API FAILED", "Failure in onFailure");
            }
        });
    }
}
