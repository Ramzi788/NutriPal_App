package com.example.nutripal.Models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularApi {
    @GET("recipes/{id}/nutritionWidget.json")
    Call<RecipeSearchResponse> getRecipeInfo(@Query("title") String title,
                                             @Query("apiKey") String apiKey);
}
