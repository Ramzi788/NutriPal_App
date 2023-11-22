package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AddNewMeal extends AppCompatActivity {

    private ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);
        backArrow = findViewById(R.id.backArrow);
        Intent intent = getIntent();
        String selectedRecipe = intent.getStringExtra("SELECTED_RECIPE");
        TextView textView = findViewById(R.id.meal_item_name_add_new_meal);
        textView.setText(selectedRecipe);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}