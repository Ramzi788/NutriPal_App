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

    @POST("/addmeal/{user_email}")
    Call<ResponseBody> addMeal(@Path("user_email") String userId, @Body Meal meal);

    @GET("/getmeals/{user_email}")
    Call<NutritionResponse> getMeals(@Path("user_email") String userId);

    @POST("/userdata/{user_email}")
    Call<UserData> setUserData(@Path("user_email") String userId, @Body UserData user);

    @GET("/userdata/{user_email}")
    Call<UserData> getUserData(@Path("user_email") String userEmail);
}