package com.example.nutripal;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.nutripal.R;

import java.util.Objects;
import java.util.zip.Inflater;

public class HomePage extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private ProgressBar progressBar, caloriesProgressBar;
    private int height, weight;
    public HomePage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ImageView profileItem = view.findViewById(R.id.profile_icon);
        ImageView settingsIcon = view.findViewById(R.id.settings_icon);
        caloriesProgressBar = view.findViewById(R.id.progressBar_calories);
        progressBar = view.findViewById(R.id.progressBar_steps); // Your progress bar ID

        if (getActivity()!= null){
            Intent intent = getActivity().getIntent();

            if (intent != null) {
                height = intent.getIntExtra("HEIGHT", -1);
                weight = intent.getIntExtra("WEIGHT", -1);
            }
        }



        //Setting the values of the progress bar
        progressBar.setMax(10000);
        progressBar.setProgress(0);
        caloriesProgressBar.setMax(3200);
        caloriesProgressBar.setProgress(0);


        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), "Step sensor not available!", Toast.LENGTH_SHORT).show();
        }

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class); // Replace with actual activity
                startActivity(intent);
            }
        });


        profileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), profileActivity.class); // Replace with actual activity
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int steps = (int) event.values[0];
            progressBar.setProgress(steps); // Assuming you start from 0 and count up to 10,000 within the day
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}