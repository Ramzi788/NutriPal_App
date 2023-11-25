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
import com.example.nutripal.Models.NutritionResponse;
import com.example.nutripal.Models.User;
import com.example.nutripal.Models.UserData;
import com.example.nutripal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePage extends Fragment implements SensorEventListener {

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int) event.values[0];
            int currentSteps = totalSteps-previewTotalSteps;
            stepsText.setText(String.valueOf(currentSteps));
            stepsProgressBar.setProgress(currentSteps);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            if (currentDate.equals(getLastUpdateDate())) {
                storeLastUpdateDate(currentDate);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    private SensorManager sensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0 , previewTotalSteps = 0;
    private TextView stepsText, currentCalories, CalorieGoal;
    private ProgressBar stepsProgressBar, caloriesProgressBar;
    private int height, weight;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Calendar calendar = Calendar.getInstance();
    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    int currentMonth = calendar.get(Calendar.MONTH) + 1;
    int currentYear = calendar.get(Calendar.YEAR);
    String currentDate = currentDay + "-" + currentMonth + "-" + currentYear;
    public HomePage() {}
    public void onResume(){
        super.onResume();
        if (stepSensor == null){
            Toast.makeText(getActivity(),"This device has no sensor", Toast.LENGTH_SHORT).show();
        }
        else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            String lastUpdateDate = getLastUpdateDate();

            if (!currentDate.equals(lastUpdateDate)) {
                // It's a new day, reset steps and store the new date
                previewTotalSteps = totalSteps;
                storeLastUpdateDate(currentDate);
            }
        }

    }

    private void storeLastUpdateDate(String date) {
        SharedPreferences preferences = requireActivity().getSharedPreferences("StepCounter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LastUpdateDate", date);
        editor.apply();
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
            value += calories;
            currentCalories.setText(Integer.toString(value));
            caloriesProgressBar.setProgress(value);
            Toast.makeText(getActivity(),Integer.toString(caloriesProgressBar.getProgress()), Toast.LENGTH_SHORT).show();
        });
    }
    private void fetchNutritionalData() {
        assert user != null;
        String userEmail = user.getEmail();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.104:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);

        api.getMeals(userEmail,currentDate).enqueue(new Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutritionResponse nutrition = response.body();
                    updateCurrentStatus(nutrition.getCalories());
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                // Handle failure
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
        int maxCalories = Integer.parseInt(CalorieGoal.getText().toString());
        caloriesProgressBar.setMax(maxCalories);
        fetchNutritionalData();
        //Setting the values of the progress bar
        stepsProgressBar.setMax(10000);
        stepsProgressBar.setProgress(0);
        caloriesProgressBar.setMax(maxCalories);
        caloriesProgressBar.setProgress(0);

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