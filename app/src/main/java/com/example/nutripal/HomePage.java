package com.example.nutripal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.Meal;
import com.example.nutripal.Models.NutritionResponse;
import com.example.nutripal.Models.StepData;
import com.example.nutripal.Models.User;
import com.example.nutripal.Models.UserData;
import com.example.nutripal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.zip.Inflater;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends Fragment implements SensorEventListener {


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            totalSteps = (int) event.values[0];
            int currentSteps = totalSteps - previewTotalSteps;
            stepsText.setText(String.valueOf(currentSteps));
            stepsProgressBar.setProgress(currentSteps);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());

            if (currentSteps % 100 == 0) {
                sendStepDataToServer(currentSteps);
            }

            if (!currentDate.equals(getLastUpdateDate())) {
                storeLastUpdateDate(currentDate);
                // Reset step count for the new day
                previewTotalSteps = totalSteps;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private SensorManager sensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0 , previewTotalSteps = 0, maxCalories;
    private TextView stepsText, currentCalories, CalorieGoal, fullName, caloriesRemaining;
    private ProgressBar stepsProgressBar, caloriesProgressBar;
    private TextView heightText, weightText, bmi, RecentMeal;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Calendar calendar = Calendar.getInstance();
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    int currentMonth = calendar.get(Calendar.MONTH) + 1;
    int currentYear = calendar.get(Calendar.YEAR);
    String currentDate = currentDay + "-" + currentMonth + "-" + currentYear;
    public HomePage() {}
    public void onResume(){
        super.onResume();

            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            String lastUpdateDate = getLastUpdateDate();

            if (!currentDate.equals(lastUpdateDate)) {
                previewTotalSteps = totalSteps;
                storeLastUpdateDate(currentDate);
        }
        if (user != null) {
            fetchUserData(user.getEmail());
            fetchNutritionalData();
            fetchStepData(user.getEmail(), currentDate);
        }

    }

    private void storeLastUpdateDate(String date) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LastUpdateDate", date);
        editor.apply();
    }
    private void sendStepDataToServer(int steps) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        StepData stepData = new StepData(currentDate, steps);
        Call<StepData> call = api.setSteps(user.getEmail(), stepData);

        call.enqueue(new Callback<StepData>() {
            @Override
            public void onResponse(Call<StepData> call, Response<StepData> response) {

            }

            @Override
            public void onFailure(Call<StepData> call, Throwable t) {

            }
        });
    }
    private void fetchStepData(String userEmail, String date) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        Call<StepData> call = api.getSteps(userEmail);

        call.enqueue(new Callback<StepData>() {
            @Override
            public void onResponse(Call<StepData> call, Response<StepData> response) {
                if(response.isSuccessful() && response.body() != null) {
                    StepData stepData = response.body();
                    stepsText.setText(String.valueOf(stepData.getSteps()));
                }
            }

            @Override
            public void onFailure(Call<StepData> call, Throwable t) {

            }
        });
    }
    private String getLastUpdateDate() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
        return preferences.getString("LastUpdateDate", "");
    }

    public void onPause(){
        super.onPause();
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
    @SuppressLint("SetTextI18n")
    private void updateCurrentStatus(int calories) {
        requireActivity().runOnUiThread(() -> {
            int value = Integer.parseInt(currentCalories.getText().toString());
            currentCalories.setText(Integer.toString(value + calories));
            caloriesProgressBar.setProgress(value + calories);
            caloriesProgressBar.animate();
            });
    }
    private void fetchNutritionalData() {
        assert user != null;
        String userEmail = user.getEmail();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.getMeals(userEmail,currentDate).enqueue(new Callback<NutritionResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutritionResponse nutrition = response.body();
                    updateCurrentStatus(nutrition.getCalories());
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {

            }
        });

    }
    private void fetchUserData(String userEmail) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        fetchCurrentCalories(userEmail, currentValue -> {
            api.getUserData(userEmail).enqueue(new Callback<UserData>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserData userData = response.body();
                        maxCalories = userData.getCalorieGoal();
                        int current = Integer.parseInt(currentCalories.getText().toString());
                        requireActivity().runOnUiThread(() -> {
                            caloriesProgressBar.setMax(maxCalories);
                            CalorieGoal.setText(String.valueOf(maxCalories));

                            if (maxCalories <= current)
                                caloriesRemaining.setText("Calories Remaining: " + 0);
                            else caloriesRemaining.setText((maxCalories - currentValue) + " Calories Remaining");
                            currentCalories.setText(String.valueOf(currentValue));
                            caloriesProgressBar.setProgress(currentValue);

                            heightText.setText("Height: " + userData.getHeight() + " cm");
                            weightText.setText("Weight: " + userData.getWeight() + " kg");

                            double height = userData.getHeight() / 100.0;
                            double weight = userData.getWeight();
                            double calculatedBMI = weight / Math.pow(height, 2);
                            bmi.setText(String.format(Locale.getDefault(), "%.1f", calculatedBMI));
                            fullName.setText("Welcome, \n" + userData.getFirstName() + " " + userData.getLastName());
                        });
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {

                }

            });
        });
    }

    private void fetchCurrentCalories(String userEmail, Consumer<Integer> onCaloriesFetched) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.4:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.getMeals(userEmail, currentDate).enqueue(new Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutritionResponse nutritionResponse = response.body();
                    int currentCalories = nutritionResponse.getCalories();
                    onCaloriesFetched.accept(currentCalories);

                } else {

                    onCaloriesFetched.accept(0);
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                onCaloriesFetched.accept(0);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ImageView logOutButton = view.findViewById(R.id.log_out);
        caloriesProgressBar = view.findViewById(R.id.progressBar_calories);
        stepsProgressBar= view.findViewById(R.id.progressBar_steps);
        stepsText = view.findViewById(R.id.stepsCounter);
        currentCalories = view.findViewById(R.id.currentCalories);
        CalorieGoal = view.findViewById(R.id.CalorieGoal);
        fullName = view.findViewById(R.id.welcomeTextView);
        caloriesRemaining = view.findViewById(R.id.caloriesRemaining);
        heightText = view.findViewById(R.id.tvHeight);
        weightText = view.findViewById(R.id.tvWeight);
        bmi = view.findViewById(R.id.tvBmiValue);
        RecentMeal = view.findViewById(R.id.recentMeal);


        stepsProgressBar.setMax(10000);
        stepsProgressBar.setProgress(0);

        sensorManager = (SensorManager)requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);




        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(getActivity(), Splash_Screen.class); // Replace with actual activity
                startActivity(intent);
            }
        });
        return view;
    }




}