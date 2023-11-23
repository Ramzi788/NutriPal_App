 package com.example.nutripal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import com.example.nutripal.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
              }  else if (itemId == R.id.navigation_more) {
//                  selectedFragment = new MoreFragment();
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
     }


 }
