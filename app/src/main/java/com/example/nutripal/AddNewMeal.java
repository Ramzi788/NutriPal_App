package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nutripal.Models.Nutrient;
import com.example.nutripal.Models.NutrientSearchResponse;
import com.example.nutripal.Models.SpoonacularApi;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewMeal extends AppCompatActivity {
    private ImageView backArrow;
    private TextView caloriesView, carbsView, fatView, proteinView; // TextViews to display the nutrients
    private ProgressBar caloriesBar;


    private void sendApiRequest(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpoonacularApi api = retrofit.create(SpoonacularApi.class);
        Call<NutrientSearchResponse> call = api.getNutrients(id, "67f24b385b80476f91f6dde402c11261");

        call.enqueue(new Callback<NutrientSearchResponse>() {
            @Override
            public void onResponse(Call<NutrientSearchResponse> call, Response<NutrientSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutrientSearchResponse searchResponse = response.body();
                    displayNutrients(searchResponse);
                } else {
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Call<NutrientSearchResponse> call, Throwable t) {
                // Handle failure, like network issues
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayNutrients(NutrientSearchResponse searchResponse) {
        Nutrient calories = searchResponse.getCalories();
        Nutrient carbs = searchResponse.getCarbs();
        Nutrient fat = searchResponse.getFat();
        Nutrient protein = searchResponse.getProtein();
        caloriesBar = findViewById(R.id.progressBar_add_new_meal);

        // Assuming you have TextViews in your layout to display these values
        caloriesView.setText(calories.getAmount() + "\n" + calories.getUnit());
        carbsView.setText(carbs.getAmount() + " " + carbs.getUnit());
        fatView.setText(fat.getAmount() + " " + fat.getUnit());
        proteinView.setText(protein.getAmount() + " " + protein.getUnit());
        caloriesBar.setProgress((int) calories.getAmount());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        // Initialize TextViews
        caloriesView = findViewById(R.id.calories_value);
        carbsView = findViewById(R.id.carbs_value);
        fatView = findViewById(R.id.fat_value);
        proteinView = findViewById(R.id.protein_value);

        backArrow = findViewById(R.id.backArrow);
        Intent intent = getIntent();
        String selectedRecipe = intent.getStringExtra("SELECTED_RECIPE");
        int selectedId = intent.getIntExtra("ItemId",-1);
        TextView textView = findViewById(R.id.meal_item_name_add_new_meal);
        textView.setText(selectedRecipe);




        // Use sendApiRequest to make the API call
        sendApiRequest(selectedId);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }
}