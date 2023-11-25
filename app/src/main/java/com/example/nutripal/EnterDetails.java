package com.example.nutripal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutripal.Models.FastAPIEndpoint;
import com.example.nutripal.Models.NutrientSearchResponse;
import com.example.nutripal.Models.SpoonacularApi;
import com.example.nutripal.Models.User;
import com.example.nutripal.Models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnterDetails extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    private LinearLayout continueButton;
    private EditText calorieGoal;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
        EditText editText = findViewById(R.id.dob_details_Screen); // Replace with your actual ID
        editText.setFocusable(false);
        continueButton = findViewById(R.id.continueButton);
        calorieGoal = findViewById(R.id.calorieGoal);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String calorieGoalInput = calorieGoal.getText().toString();
                assert user != null;
                if (!calorieGoalInput.isEmpty()) {
                    int goal = Integer.parseInt(calorieGoalInput);
                    sendApiRequest(user.getEmail(), goal);
                } else {
                    Toast.makeText(EnterDetails.this, "Please enter a calorie goal", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(EnterDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(EnterDetails.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                    private void updateLabel() {
                        String myFormat = "dd/MM/yyyy"; // Your desired format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        editText.setText(sdf.format(myCalendar.getTime()));
                    }

                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void sendApiRequest(String id, int calorieGoal) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FastAPIEndpoint api = retrofit.create(FastAPIEndpoint.class);
        UserData userData = new UserData(calorieGoal);
        api.setUserData(id, userData).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }


        });
    }
}