package com.example.nutripal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.NutritionResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackerSection extends Fragment {
    private ProgressBar carbsBar, proteinBar, fatBar, progressBarFiber;
    private TextView currentCarbs, currentProtein, currentFat, currentFiber;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Calendar calendar = Calendar.getInstance();
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    int currentMonth = calendar.get(Calendar.MONTH) + 1;
    int currentYear = calendar.get(Calendar.YEAR);
    String currentDate = currentDay + "-" + currentMonth + "-" + currentYear;
    public TrackerSection() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trackers, container, false);
        carbsBar = view.findViewById(R.id.progressBarCarbs);
        proteinBar = view.findViewById(R.id.progressBarProtein);
        fatBar = view.findViewById(R.id.progressBarFat);
        progressBarFiber = view.findViewById(R.id.progressBarFiber);
        currentCarbs = view.findViewById(R.id.textViewCarbs);
        currentProtein = view.findViewById(R.id.textViewProtein);
        currentFat = view.findViewById(R.id.textViewFat);
        currentFiber = view.findViewById(R.id.textViewFiber);

        fetchNutritionalData();

        return view;

    }
    private void fetchNutritionalData() {
        assert user != null;
        String userEmail = user.getEmail();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.getMeals(userEmail, currentDate).enqueue(new Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutritionResponse nutrition = response.body();
                    updateProgressBars(nutrition.getCarbs(), nutrition.getProtein(), nutrition.getFat(), nutrition.getFiber());
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateProgressBars(int carbs, int protein, int fat, int fiber) {
        requireActivity().runOnUiThread(() -> {
            carbsBar.setProgress(carbs);
            proteinBar.setProgress(protein);
            fatBar.setProgress(fat);
            progressBarFiber.setProgress(fiber);
            int carbsVal = Integer.parseInt(currentCarbs.getText().toString());
            currentCarbs.setText(carbsVal + carbs + " g");
            int proteinVal = Integer.parseInt(currentProtein.getText().toString());
            currentProtein.setText(proteinVal + protein + " g");
            int fatVal = Integer.parseInt(currentFat.getText().toString());
            currentFat.setText(fatVal + fat + " g");
            int fiberVal = Integer.parseInt(currentFiber.getText().toString());
            currentFiber.setText(fiberVal + fiber + " g");
        });
    }
}