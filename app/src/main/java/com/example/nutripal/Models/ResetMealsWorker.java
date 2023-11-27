package com.example.nutripal.Models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResetMealsWorker extends Worker {

    public ResetMealsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8000") // Your API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FastAPIEndpoint service = retrofit.create(FastAPIEndpoint.class);

        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userEmail = user.getEmail();
            service.resetMeals(userEmail).execute();
            return Result.success();
        } catch (Exception e) {
            return Result.failure();
        }
    }

    private String getUserEmail() {
        // Implement this method to get the user email
        return "user@example.com";
    }
}
