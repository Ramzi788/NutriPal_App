package com.example.nutripal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;


public class Meal_Page extends Fragment {
    public Meal_Page(){}

    String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diary_page, container, false);


        TextView textViewDiary = view.findViewById(R.id.diary_text);
        TextView textViewTrackers = view.findViewById(R.id.trackers_text);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.footer, new DiarySection())
                .commit();

        textViewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDiary.setSelected(true);
                textViewTrackers.setSelected(false);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.footer, new DiarySection())
                        .commit();
            }
        });

        textViewTrackers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDiary.setSelected(false);
                textViewTrackers.setSelected(true);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.footer, new TrackerSection())
                        .commit();
            }
        });


        textViewDiary.setSelected(true);

        return view;

    }
}