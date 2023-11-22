package com.example.nutripal.Models;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FastAPIEndpoint {
    @POST("register/")
    Call<ResponseBody> registerUser(@Body User user);
}