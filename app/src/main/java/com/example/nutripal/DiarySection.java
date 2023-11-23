package com.example.nutripal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DiarySection extends Fragment {
    public DiarySection() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_meal, container, false);
        LinearLayout box =view.findViewById(R.id.add_meal);
        LinearLayout bkfst_box = view.findViewById(R.id.breakfast_box);
        LinearLayout lunch_box = view.findViewById(R.id.lunch_box);
        LinearLayout dinner_box = view.findViewById(R.id.dinner_box);
        LinearLayout snack_box = view.findViewById(R.id.snacks_box);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMeal.class);
                startActivity(intent);
            }
        });

        bkfst_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.breakfas);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        lunch_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.lunch);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        dinner_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.Dinner);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        snack_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealSummary.class);
                TextView text = view.findViewById(R.id.Snack);
                intent.putExtra("TITLE",text.getText().toString());
                startActivity(intent);
            }
        });
        return view;
    }
}
