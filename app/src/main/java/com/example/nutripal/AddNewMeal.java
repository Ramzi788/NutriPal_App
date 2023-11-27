package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.Meal;
import com.example.nutripal.Models.Nutrient;
import com.example.nutripal.Models.NutrientSearchResponse;
import com.example.nutripal.Models.NutritionResponse;
import com.example.nutripal.Models.ProductResponse;
import com.example.nutripal.Models.SpoonacularApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewMeal extends AppCompatActivity {
    private ImageView backArrow, checkMark;
    private TextView caloriesView, carbsView, fatView, proteinView, textView ; // TextViews to display the nutrients
    private ProgressBar caloriesBar;
    private EditText numberOfServings;
    private String groceryName;
    private Spinner mealTypeSpinner;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private NutrientSearchResponse latestNutrientSearchResponse = null;
    private ProductResponse latestProductResponse = null;

    Calendar calendar = Calendar.getInstance();
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    int currentMonth = calendar.get(Calendar.MONTH) + 1;
    int currentYear = calendar.get(Calendar.YEAR);

    private void fetchGroceryDetails(String upcCode) {
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
                    latestProductResponse = response.body();
                    updateNutrientValues();
                } else {
                    Log.e("API Error", "Response not successful");
                }
            }
            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("API Error", "Network failure", t);
            }
        });
    }
    private float extractNumericValue(String text) {
        // Extracts the numeric part from a string (e.g., "230.0g" -> "230.0")
        return Float.parseFloat(text.replaceAll("[^0-9.]", ""));
    }
    private void displayGroceryNutrients(ProductResponse response, int numberOfServings) {
        ProductResponse.Nutrition nutrition = response.getNutrition();
        if (nutrition != null) {
            String calories = nutrition.getCalories();
            String protein = nutrition.getProtein();
            String fat = nutrition.getFat();
            String carbs = nutrition.getCarbs();
            textView.setText(response.getTitle());
            caloriesView.setText(calories);
            proteinView.setText(protein);
            fatView.setText(fat);
            carbsView.setText(carbs);
        }
        checkMark = findViewById(R.id.checkMark);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.169.57.171:8000") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meal newMeal = null;
                Log.d("CheckMark", "Check mark clicked");
                int servings = numberOfServings;

                if (latestProductResponse != null) {
                    int totalCalories = (int) (servings * extractNumericValue(caloriesView.getText().toString()));
                    int totalCarbs = (int) (servings * extractNumericValue(carbsView.getText().toString()));
                    int totalFat = (int) (servings * extractNumericValue(fatView.getText().toString()));
                    int totalProtein = (int) (servings * extractNumericValue(proteinView.getText().toString()));
                    newMeal = new Meal(textView.getText().toString(), totalCalories, totalCarbs, totalFat, totalProtein, 0, currentDay, currentMonth, currentYear, mealTypeSpinner.getSelectedItem().toString());
                }


                if (newMeal != null && user != null) {
                    String userEmail = user.getEmail();
                    api.addMeal(userEmail, newMeal).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddNewMeal.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddNewMeal.this, "Failed to add meal", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(AddNewMeal.this, "Error adding meal", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (newMeal == null) {
                    Toast.makeText(AddNewMeal.this, "Meal data is incomplete", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddNewMeal.this, "No User logged in", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

    }

    private void sendApiRequest(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpoonacularApi api = retrofit.create(SpoonacularApi.class);
        Call<NutrientSearchResponse> call = api.getNutrients(id, "1ad3a28a071c4370adc91a6cd4990705");

        call.enqueue(new Callback<NutrientSearchResponse>() {
            @Override
            public void onResponse(Call<NutrientSearchResponse> call, Response<NutrientSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutrientSearchResponse searchResponse = response.body();
                    latestNutrientSearchResponse = searchResponse;
                    updateNutrientValues();
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
    private void updateNutrientValues() {
        String servingsText = numberOfServings.getText().toString();
        int servings = servingsText.isEmpty() ? 1 : Integer.parseInt(servingsText);

        if (latestNutrientSearchResponse != null) {
            displayNutrients(latestNutrientSearchResponse, servings);
        }

        if (latestProductResponse != null) {
            displayGroceryNutrients(latestProductResponse, servings);
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayNutrients(NutrientSearchResponse searchResponse, int numberOfServings) {
        Nutrient calories = searchResponse.getCalories();
        Nutrient carbs = searchResponse.getCarbs();
        Nutrient fat = searchResponse.getFat();
        Nutrient protein = searchResponse.getProtein();
        Nutrient fiber = searchResponse.getFiber();
        caloriesBar = findViewById(R.id.progressBar_add_new_meal);

        @SuppressLint("DefaultLocale") String caloriesText = String.format("%.1f %s", numberOfServings * calories.getAmount(), "\n" +calories.getUnit());
        @SuppressLint("DefaultLocale") String carbsText = String.format("%.1f %s", numberOfServings * carbs.getAmount(), carbs.getUnit());
        @SuppressLint("DefaultLocale") String fatText = String.format("%.1f %s", numberOfServings * fat.getAmount(), fat.getUnit());
        @SuppressLint("DefaultLocale") String proteinText = String.format("%.1f %s", numberOfServings * protein.getAmount(), protein.getUnit());

        caloriesView.setText(caloriesText);
        carbsView.setText(carbsText);
        fatView.setText(fatText);
        proteinView.setText(proteinText);

        checkMark = findViewById(R.id.checkMark);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.169.57.171:8000") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meal newMeal = null;
                Log.d("CheckMark", "Check mark clicked");
                int servings = numberOfServings;
                if (latestNutrientSearchResponse != null) {
                    int totalCalories = (int)(servings * latestNutrientSearchResponse.getCalories().getAmount());
                    int totalCarbs = (int)(servings * latestNutrientSearchResponse.getCarbs().getAmount());
                    int totalFat = (int)(servings * latestNutrientSearchResponse.getFat().getAmount());
                    int totalProtein = (int)(servings * latestNutrientSearchResponse.getProtein().getAmount());
                    newMeal = new Meal(textView.getText().toString(),totalCalories, totalCarbs, totalFat, totalProtein, (int) fiber.getAmount(), currentDay, currentMonth, currentYear, mealTypeSpinner.getSelectedItem().toString());
                }

                if (newMeal != null && user != null) {
                    String userEmail = user.getEmail();
                    api.addMeal(userEmail, newMeal).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(AddNewMeal.this, "Meal added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddNewMeal.this, "Failed to add meal", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(AddNewMeal.this, "Error adding meal", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (newMeal == null) {
                    Toast.makeText(AddNewMeal.this, "Meal data is incomplete", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddNewMeal.this, "No User logged in", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);

        //TextViews
        caloriesView = findViewById(R.id.calories_value);
        carbsView = findViewById(R.id.carbs_value);
        fatView = findViewById(R.id.fat_value);
        proteinView = findViewById(R.id.protein_value);
        numberOfServings = findViewById(R.id.numberOfServings);
        textView = findViewById(R.id.meal_item_name_add_new_meal);


        if (numberOfServings.getText().toString().isEmpty()){
            numberOfServings.setText(Integer.toString(1));
        }

        numberOfServings.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used, but must be implemented
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not used, but must be implemented
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateNutrientValues();
            }
        });
        mealTypeSpinner = findViewById(R.id.meal_type_spinner);


        backArrow = findViewById(R.id.backArrow);
        Intent intent = getIntent();
        handleIntentData(intent);



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }
    private void handleIntentData(Intent intent) {
        if (intent.hasExtra("SELECTED_RECIPE")) {
            String selectedRecipe = intent.getStringExtra("SELECTED_RECIPE");
            int recipeId = intent.getIntExtra("ItemId", -1);
            textView.setText(selectedRecipe);
            if (recipeId != -1) {
                sendApiRequest(recipeId); // Fetch recipe details based on ID
            }
        } else if (intent.hasExtra("GroceryUPC")) {
            groceryName = intent.getStringExtra("GroceryUPC");
            fetchGroceryDetails(groceryName);
        }
        else if (getIntent().hasExtra("MEAL_NAME")) {
            String mealName = getIntent().getStringExtra("MEAL_NAME");
            textView.setText(mealName);
            int recipeID = intent.getIntExtra("ITEM_ID", -1);
            sendApiRequest(recipeID);
        }
    }

}
