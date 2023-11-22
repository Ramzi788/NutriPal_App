package com.example.spoontest;

import com.example.spoontest.Models.Recipe;
import com.example.spoontest.Models.RecipeSearchResponse;
import com.example.spoontest.Models.Nutrient;
import com.example.spoontest.Models.NutrientSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularApi {
    @GET("recipes/complexSearch")
    Call<RecipeSearchResponse> getRecipeInfo(@Query("query") String query,
                                             @Query("apiKey") String apiKey);

    @GET("recipes/{id}/nutritionWidget.json")
    Call<NutrientSearchResponse> getNutrients(@Path("id") int recipeId, @Query("apiKey") String apiKey);
}
