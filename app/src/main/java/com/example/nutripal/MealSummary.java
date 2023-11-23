package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MealSummary extends AppCompatActivity {
    private TextView title;
    private ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_summary);
        title = findViewById(R.id.meal_summary_text_food_summary);
        backArrow = findViewById(R.id.backArrow);
        Intent intent = getIntent();
        String title_top = intent.getStringExtra("TITLE");
        title.setText(title_top);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}