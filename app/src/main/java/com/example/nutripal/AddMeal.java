package com.example.nutripal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutripal.Models.Recipe;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddMeal extends AppCompatActivity {
    List<Recipe> recipes;
    public void fetchItemsFromApi(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestManager.CallComplexSearch service = retrofit.create(RequestManager.CallComplexSearch.class);
        Call<List<Recipe>> call = service.autocompleteRecipes(query,String.valueOf(R.string.api_key));

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                     recipes = response.body();
                    // Update your UI here with the list of recipes
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        ImageView backArrow = findViewById(R.id.backArrow);
        List<Recipe> items = recipes;
        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        AutoCompleteTextView textView = findViewById(R.id.searchEditText);
        textView.setAdapter(adapter);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    fetchItemsFromApi(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to the previous one
                finish();
            }
        });

    }
}
