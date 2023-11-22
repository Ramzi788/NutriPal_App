package com.example.spoontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.spoontest.Models.Nutrient;
import com.example.spoontest.Models.NutrientSearchResponse;
import com.example.spoontest.Models.Recipe;
import com.example.spoontest.Models.RecipeSearchResponse;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCalories extends AppCompatActivity {

    private ListView listViewNutrients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calories);
        listViewNutrients = findViewById(R.id.listViewNutrients);
        sendApiRequest(getIntent().getIntExtra("ItemId", -1));
    }

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
                    // Handle successful response here
                    NutrientSearchResponse searchResponse = response.body();
                    handleSuccessfulResponse(searchResponse);
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

    private void handleSuccessfulResponse(NutrientSearchResponse searchResponse) {
        List<Nutrient> nutrients = searchResponse.getNutrients();
        ArrayAdapter<Nutrient> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, nutrients);
        listViewNutrients.setAdapter(adapter);

        // Update your ListView adapter or other UI elements here
        // Example: listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipeTitles));
    }
}