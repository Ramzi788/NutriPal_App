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
    DayScrollDatePicker datePicker;
    String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diary_page, container, false);

        datePicker = view.findViewById(R.id.dayDatePicker);
        Calendar calendar = Calendar.getInstance();
        datePicker.setStartDate(calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
        datePicker.getSelectedDate(new OnDateSelectedListener() {
           @Override
           public void onDateSelected(@Nullable Date date) {
               selectedDate = date.toString();
               Toast.makeText(getActivity(), selectedDate, Toast.LENGTH_SHORT).show();
           }
       });


        TextView textViewDiary = view.findViewById(R.id.diary_text);
        TextView textViewTrackers = view.findViewById(R.id.trackers_text);

        // Initialize the diary section fragment
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