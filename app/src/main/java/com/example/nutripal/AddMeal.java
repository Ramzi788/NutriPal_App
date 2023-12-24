package com.example.nutripal;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.Meal;
import com.example.nutripal.Models.MealAdapter;
import com.example.nutripal.Models.MealEaten;
import com.example.nutripal.Models.Nutrient;
import com.example.nutripal.Models.NutritionResponse;
import com.example.nutripal.Models.ProductResponse;
import com.example.nutripal.Models.Recipe;
import com.example.nutripal.Models.RecipeSearchResponse;
import com.example.nutripal.Models.SpoonacularApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class AddMeal extends AppCompatActivity {
    private List<Recipe> recipesList;
    private ListView historyList;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private LinearLayout barcode;
    private static final int BARCODE_SCANNER_REQUEST_CODE = 1;
    private void sendApiRequest(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        SpoonacularApi api = retrofit.create(SpoonacularApi.class);
        Call<RecipeSearchResponse> call = api.getRecipeInfo(query, "1ad3a28a071c4370adc91a6cd4990705");

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

            }
        });
    }


    private void handleSuccessfulResponse(RecipeSearchResponse searchResponse) {
        recipesList = searchResponse.getResults();
        List<String> recipeTitles = new ArrayList<>();

        for (Recipe recipe : recipesList) {
            recipeTitles.add(recipe.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, recipeTitles);
        textView.setAdapter(adapter);
        textView.showDropDown();
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(AddMeal.this, AddNewMeal.class);
                intent.putExtra("SELECTED_RECIPE", selectedItem);
                String query = String.valueOf(textView.getText());
                int itemId = getIdForQuery(query);
                intent.putExtra("ItemId", itemId);
                startActivity(intent);
                textView.setText("");
            }
        });
    }


    private AutoCompleteTextView textView;


    @Override
    protected void onResume() {
        super.onResume();
        user = auth.getCurrentUser();
        if(user != null)
            fetchNutritionalData();
    }
    private void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            fetchProductDetails(result.getContents());
        }
    });
    private void fetchProductDetails(String upcCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpoonacularApi api = retrofit.create(SpoonacularApi.class);
        Call<ProductResponse> call = api.getProductByUPC(upcCode, "1ad3a28a071c4370adc91a6cd4990705");

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();
                    Intent intent = new Intent(AddMeal.this, AddNewMeal.class);
                    intent.putExtra("GroceryName", productResponse.getTitle());
                    intent.putExtra("GroceryUPC", upcCode);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("API Error", "Response not successful");
            }
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        ImageView backArrow = findViewById(R.id.backArrow);
        historyList = findViewById(R.id.historyList);
        textView = findViewById(R.id.searchEditText);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        barcode = findViewById(R.id.addBarcode);
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        if(user != null)
            fetchNutritionalData();

        textView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedRecipe = (String) adapterView.getItemAtPosition(position);
            textView.setText(selectedRecipe);
        });
        textView.setOnClickListener(view -> {
            String query = textView.getText().toString();
            if (!query.isEmpty()) {
                sendApiRequest(query);
            }

        });
        textView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // Index of the right drawable

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (textView.getRight() - textView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Clear the text when the "X" icon is clicked
                        textView.setText("");
                        return true;
                    }
                }
                return false;
            }
        });





        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private int getIdForQuery(String query) {
        for (Recipe recipe : recipesList) {
            if (recipe.getTitle().equalsIgnoreCase(query)) {
                return recipe.getId();
            }
        }
        return -1; // return an invalid ID or throw an exception if no match is found
    }

    private void fetchNutritionalData() {
        assert user != null;
        String userEmail = user.getEmail();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.IP))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.getAllMeals(userEmail).enqueue(new Callback<List<MealEaten>>() {
            @Override
            public void onResponse(Call<List<MealEaten>> call, Response<List<MealEaten>> response) {
                if(response.isSuccessful()){
                    List<MealEaten> meals = response.body();
                    MealEaten[] displayList = new MealEaten[meals.size()];
                    for(int i = 0; i < displayList.length; i++)
                        displayList[i] = meals.get(i);
                    MealAdapter adapter = new MealAdapter(AddMeal.this, meals);
                    adapter.notifyDataSetChanged();
                    historyList.setAdapter(adapter);
                    historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MealEaten selectedMeal = (MealEaten) parent.getItemAtPosition(position);
                            openAddNewMealActivity(selectedMeal);
                        }
                    });
                }
                else{
                    Log.d("ALLMEALS API: ", "Failed, in on Response block");
                }
            }

            @Override
            public void onFailure(Call<List<MealEaten>> call, Throwable t) {
                Log.d("ALLMEALS API: ", "Failed, in on failure block");
            }
        });

    }
    private void openAddNewMealActivity(MealEaten meal) {
        Intent intent = new Intent(AddMeal.this, AddNewMeal.class);
        intent.putExtra("MEAL_NAME", meal.getName());
        intent.putExtra("ITEM_ID", meal.getId());
        startActivity(intent);
    }

}