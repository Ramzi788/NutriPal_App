package com.example.nutripal.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.nutripal.Models.MealEaten;
import com.example.nutripal.R;

import java.util.List;


public class MealAdapter extends ArrayAdapter<MealEaten> {

    public MealAdapter(Context context, List<MealEaten> meals) {
        super(context, 0, meals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MealEaten meal = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.displaytext);
        TextView tvCalories = convertView.findViewById(R.id.displayCalories);

        tvName.setText(meal.getName());
        tvCalories.setText(String.valueOf(meal.getCalories()));

        return convertView;
    }
}
