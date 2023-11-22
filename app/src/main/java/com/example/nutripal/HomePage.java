package com.example.nutripal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.nutripal.R;

import java.util.zip.Inflater;

public class HomePage extends Fragment {
    public HomePage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ImageView profileItem = view.findViewById(R.id.profile_icon);
        ImageView settingsIcon = view.findViewById(R.id.settings_icon);

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Settings.class); // Replace with actual activity
                startActivity(intent);
            }
        });


        profileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), profileActivity.class); // Replace with actual activity
                startActivity(intent);
            }
        });
        return view;
    }
}