package com.example.nutripal;

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

import com.example.nutripal.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.Inflater;

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
    private TextView stepsText;
    private ProgressBar stepsProgressBar, caloriesProgressBar;
    private int height, weight;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ImageView profileItem = view.findViewById(R.id.profile_icon);
        ImageView settingsIcon = view.findViewById(R.id.settings_icon);
        caloriesProgressBar = view.findViewById(R.id.progressBar_calories);
        stepsProgressBar= view.findViewById(R.id.progressBar_steps);
        stepsText = view.findViewById(R.id.stepsCounter);

        //Setting the values of the progress bar
        stepsProgressBar.setMax(10000);
        stepsProgressBar.setProgress(0);
        caloriesProgressBar.setMax(3200);
        caloriesProgressBar.setProgress(0);

        sensorManager = (SensorManager)requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);



        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class); // Replace with actual activity
                startActivity(intent);
            }
        });
        if (getActivity()!= null){
            Intent intent = getActivity().getIntent();

            if (intent != null) {
                height = intent.getIntExtra("HEIGHT", -1);
                weight = intent.getIntExtra("WEIGHT", -1);
            }
        }


        profileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), profileActivity.class); // Replace with actual activity
                startActivity(intent);
            }
        });
        return view;
    }




}