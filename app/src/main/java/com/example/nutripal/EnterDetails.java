package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EnterDetails extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    private LinearLayout continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
        EditText editText = findViewById(R.id.dob_details_Screen); // Replace with your actual ID
        editText.setFocusable(false);
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterDetails.this, HomePage.class);
                intent.putExtra("HEIGHT", R.id.heightInput);
                intent.putExtra("WEIGHT", R.id.weightInput);
                startActivity(intent);
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EnterDetails.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        String myFormat = "dd/MM/yyyy"; // Your desired format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        editText.setText(sdf.format(myCalendar.getTime()));
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}