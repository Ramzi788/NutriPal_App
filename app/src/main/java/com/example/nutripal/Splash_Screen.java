package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

public class Splash_Screen extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private LinearLayout dotsLayout;
    private ImageView[] dots;
    private LinearLayout login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        viewFlipper = findViewById(R.id.viewFlipper);
        dotsLayout = findViewById(R.id.dotsLayout);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash_Screen.this, LoginPage.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash_Screen.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        int numberOfImages = 3; // Assuming you have 3 images in the ViewFlipper
        dots = new ImageView[numberOfImages];

        for (int i = 0; i < numberOfImages; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot)); // First dot is active

        // Set a listener to update the dots as the ViewFlipper changes views
        viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                // When a new view starts to come in, update the dot
                int newActiveIndex = (viewFlipper.getDisplayedChild());
                updateDots(newActiveIndex);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {}
        });
    }

    private void updateDots(int activeIndex) {
        for (int i = 0; i < dots.length; i++) {
            if (i == activeIndex) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
            }
        }
    }
}