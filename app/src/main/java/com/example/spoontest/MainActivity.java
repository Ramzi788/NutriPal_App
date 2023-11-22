package com.example.spoontest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.spoontest.Models.Recipe;
import com.example.spoontest.Models.RecipeSearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private List<String> recipeTitles;
    private List<Recipe> recipes;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here for this use case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel the previous request if it hasn't started
                handler.removeCallbacks(runnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();

                // Delaying the API request by 500 milliseconds after the user stops typing
                handler.postDelayed(runnable = () -> {
                    if (!query.isEmpty()) {
                        sendApiRequest(query); // To update the AutoComplete suggestions
                    }
                }, 500);
            }
        });
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                autoCompleteTextView.setText(selectedItem);
                autoCompleteTextView.dismissDropDown();
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ShowCalories.class);
                String query = String.valueOf(autoCompleteTextView.getText());
                int itemId = getIdForQuery(query); // You need to implement this method
                intent.putExtra("ItemId", itemId);
                startActivity(intent);
            }
        });
    }
    private void sendApiRequest(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpoonacularApi api = retrofit.create(SpoonacularApi.class);
        Call<RecipeSearchResponse> call = api.getRecipeInfo(query, "67f24b385b80476f91f6dde402c11261");

        call.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Handle successful response here
                    RecipeSearchResponse searchResponse = response.body();
                    handleSuccessfulResponse(searchResponse);
                } else {
                    System.out.println("Failed");
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                // Handle failure, like network issues
            }
        });
    }

    private void handleSuccessfulResponse(RecipeSearchResponse searchResponse) {
        recipes = searchResponse.getResults();
        recipeTitles = new ArrayList<>();
        for (Recipe recipe : recipes) {
            recipeTitles.add(recipe.getTitle());
        }
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) autoCompleteTextView.getAdapter();
        adapter.clear();
        adapter.addAll(recipeTitles);
        adapter.notifyDataSetChanged();
    }
    private int getIdForQuery(String query) {
        for (Recipe recipe : recipes) {
            if (recipe.getTitle().equalsIgnoreCase(query)) {
                return recipe.getId();
            }
        }
        return -1; // return an invalid ID or throw an exception if no match is found
    }

}