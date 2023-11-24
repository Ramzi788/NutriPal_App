package com.example.nutripal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.NutritionResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackerSection extends Fragment {
    private ProgressBar carbsBar, proteinBar, fatBar;
    public TrackerSection() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trackers, container, false);
        carbsBar = view.findViewById(R.id.progressBarCarbs);
        proteinBar = view.findViewById(R.id.progressBarProtein);
        fatBar = view.findViewById(R.id.progressBarFat);


        return view;

    }
    private void fetchNutritionalData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.getMeals(userId).enqueue(new Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutritionResponse nutrition = response.body();
                    updateProgressBars(nutrition.getCarbs(), nutrition.getProtein(), nutrition.getFat());
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void updateProgressBars(int carbs, int protein, int fat) {
        requireActivity().runOnUiThread(() -> {
            carbsBar.setProgress(carbs);
            proteinBar.setProgress(protein);
            fatBar.setProgress(fat);
        });
    }
}