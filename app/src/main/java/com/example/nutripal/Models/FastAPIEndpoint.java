package com.example.nutripal.Models;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FastAPIEndpoint {
    @POST("/register/")
    Call<ResponseBody> registerUser(@Body User user);

    @POST("/addmeal/{user_id}")
    Call<ResponseBody> addMeal(@Path("user_id") String userId, @Body Meal meal);

    @GET("/getmeals/{user_id}")
    Call<NutritionResponse> getMeals(@Path("user_id") String userId);
}