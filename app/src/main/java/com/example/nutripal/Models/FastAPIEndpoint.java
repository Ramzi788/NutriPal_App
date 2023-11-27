package com.example.nutripal.Models;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FastAPIEndpoint {
    @POST("/register/")
    Call<ResponseBody> registerUser(@Body User user);
    @POST("/addmeal/{user_email}")
    Call<ResponseBody> addMeal(@Path("user_email") String userId, @Body Meal meal);
    @GET("/getmeals/{user_email}/{date}")
    Call<NutritionResponse> getMeals(@Path("user_email") String userId, @Path("date") String date);
    @POST("/userdata/{user_email}")
    Call<UserData> setUserData(@Path("user_email") String userEmail, @Body UserData user);
    @GET("/userdata/{user_email}")
    Call<UserData> getUserData(@Path("user_email") String userEmail);
    @POST("/waterdata/{user_email}")
    Call<Water> setWaterData(@Path("user_email") String userEmail, @Body Water water);
    @GET("/waterdata/{user_email}")
    Call<Water> getWaterData(@Path("user_email") String userEmail);
    @GET("/userdata/{user_email}/{mealType}")
    Call<List<MealEaten>> get_meals_eaten(@Path("user_email") String userEmail, @Path("mealType") String mealType);
    @GET("/meals/{user_email}")
    Call<List<MealEaten>> getAllMeals(@Path("user_email") String userEmail);
    @DELETE("/reset_meals/{user_email}")
    Call<Void> resetMeals(@Path("user_email") String userEmail);

}