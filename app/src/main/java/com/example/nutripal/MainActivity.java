 package com.example.nutripal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nutripal.Models.ResetMealsWorker;
import com.example.nutripal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

 public class MainActivity extends AppCompatActivity {


     private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
             = new BottomNavigationView.OnNavigationItemSelectedListener() {
     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         Fragment selectedFragment = null;
         int itemId = item.getItemId();

         if (itemId == R.id.navigation_home) {
             selectedFragment = new HomePage();
         } else if (itemId == R.id.navigation_diary) {
             selectedFragment = new Meal_Page();
              }
         if (selectedFragment != null) {
             getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).commit();
         }
         return true;
     }
     };
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.main_navigation);


         BottomNavigationView navView = findViewById(R.id.bottom_navigation);
         navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

         // Assuming you want to open the HomePage first
         getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomePage()).commit();
         scheduleResetMealsWork();
     }
     private void scheduleResetMealsWork() {
         Constraints constraints = new Constraints.Builder()
                 .setRequiredNetworkType(NetworkType.CONNECTED)
                 .build();

         PeriodicWorkRequest resetMealsRequest = new PeriodicWorkRequest.Builder(ResetMealsWorker.class, 24, TimeUnit.HOURS)
                 .setConstraints(constraints)
                 // You can add initial delay calculation here if needed
                 .build();

         WorkManager.getInstance(this).enqueueUniquePeriodicWork("resetMeals", ExistingPeriodicWorkPolicy.KEEP, resetMealsRequest);
     }


 }
