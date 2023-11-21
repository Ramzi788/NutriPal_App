package com.example.nutripal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class DiarySection extends Fragment {
    public DiarySection() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_meal, container, false);
        LinearLayout box =view.findViewById(R.id.add_meal);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddMeal.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
