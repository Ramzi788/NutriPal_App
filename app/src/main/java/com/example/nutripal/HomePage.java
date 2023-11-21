package com.example.nutripal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.nutripal.R;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

import java.util.zip.Inflater;

public class HomePage extends Fragment {
    public HomePage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        Button button = view.findViewById(R.id.button);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        User user = document.toObject(User.class);
                        String username = user.getUsername();
                        // Update TextView with the username
                        TextView textView = view.findViewById(R.id.welcomeTextView);
                        textView.setText("Welcome, \n" + username + "!");
                    } else {
                        Log.d("Retrieving username", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            });
        } else {
            // Handle the case where there is no logged-in user
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegisterPage.class);
                startActivity(intent);
            }
        });
        return view;
    }
}