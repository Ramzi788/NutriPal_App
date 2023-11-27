package com.example.nutripal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private int drinkQuantity;
    String currentDate = currentDay + "-" + currentMonth + "-" + currentYear;
    public TrackerSection() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trackers, container, false);
        LinearLayout waterConsumed = view.findViewById(R.id.waterConsumed);
        carbsBar = view.findViewById(R.id.progressBarCarbs);
        proteinBar = view.findViewById(R.id.progressBarProtein);
        fatBar = view.findViewById(R.id.progressBarFat);
        progressBarFiber = view.findViewById(R.id.progressBarFiber);
        currentCarbs = view.findViewById(R.id.textViewCarbs);
        currentProtein = view.findViewById(R.id.textViewProtein);
        currentFat = view.findViewById(R.id.textViewFat);
        currentFiber = view.findViewById(R.id.textViewFiber);

        waterConsumed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWaterIntakePopup();
            }
        });

        fetchNutritionalData();



        return view;

    }
    private void showWaterIntakePopup() {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.activity_water_intake_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(popupView);
        final AlertDialog dialog = builder.create();

        SeekBar seekBarWaterIntake = popupView.findViewById(R.id.sb_water_intake);
        final TextView quantityIndicator = popupView.findViewById(R.id.tv_quantity_indicator);
        seekBarWaterIntake.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drinkQuantity = progress;
                quantityIndicator.setText(String.format("%d ml", drinkQuantity));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button submitButton = popupView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void fetchNutritionalData() {
        assert user != null;
        String userEmail = user.getEmail();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
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