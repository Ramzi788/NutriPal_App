package com.example.nutripal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.DatePicker;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import java.util.Calendar;

public class profileActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private EditText sexEditText;
    private EditText locationEditText;
    private EditText dobEditText;
    private int userHeight;
    private int userWeight;
    private String userSex;
    private String userLocation;
    private String userDob;
    private ImageView backToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        userNameEditText = findViewById(R.id.editUserName);
        heightEditText = findViewById(R.id.editHeight);
        weightEditText = findViewById(R.id.editWeight);
        sexEditText = findViewById(R.id.editSex);
        locationEditText = findViewById(R.id.editLocation);
        dobEditText = findViewById(R.id.editDob);
        backToSettings=findViewById(R.id.backToSettings);
        // Load saved data if exists
        SharedPreferences prefs = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        String userName = prefs.getString("UserName", "Nouraholic");
        userHeight = prefs.getInt("UserHeight", 159);
        userWeight = prefs.getInt("UserWeight", 60);
        userSex = prefs.getString("UserSex", "Female");
        userLocation = prefs.getString("UserLocation", "Lebanon");
        userDob = prefs.getString("UserDob", "01/01/2000"); // Default to "01/01/2000"

        userNameEditText.setText(userName);
        heightEditText.setText(String.format("%d cm", userHeight));
        weightEditText.setText(String.format("%d kg", userWeight));
        sexEditText.setText(userSex);
        locationEditText.setText(userLocation);
        dobEditText.setText(userDob);


        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(profileActivity.this,Settings.class);
                startActivity(intent);
            }
        });
        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    saveUserName();
                }
            }
        });

        heightEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeightPickerDialog();
            }
        });

        weightEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeightPickerDialog();
            }
        });

        sexEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexPickerDialog();
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationPickerDialog();
            }
        });
        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void saveUserName() {
        String userName = userNameEditText.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences("ProfilePrefs", MODE_PRIVATE).edit();
        editor.putString("UserName", userName);
        editor.apply();
    }

    private void showHeightPickerDialog() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(100); // Minimum height
        numberPicker.setMaxValue(250); // Maximum height
        numberPicker.setValue(userHeight); // Set the current height

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Height");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userHeight = numberPicker.getValue();
                heightEditText.setText(String.format("%d cm", userHeight));
                saveHeight();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(numberPicker);
        builder.show();
    }

    private void saveHeight() {
        SharedPreferences.Editor editor = getSharedPreferences("ProfilePrefs", MODE_PRIVATE).edit();
        editor.putInt("UserHeight", userHeight);
        editor.apply();
    }

    private void showWeightPickerDialog() {
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(30); // Minimum weight
        numberPicker.setMaxValue(200); // Maximum weight
        numberPicker.setValue(userWeight); // Set the current weight

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Weight");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userWeight = numberPicker.getValue();
                weightEditText.setText(String.format("%d kg", userWeight));
                saveWeight();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setView(numberPicker);
        builder.show();
    }

    private void saveWeight() {
        SharedPreferences.Editor editor = getSharedPreferences("ProfilePrefs", MODE_PRIVATE).edit();
        editor.putInt("UserWeight", userWeight);
        editor.apply();
    }
    private void showSexPickerDialog() {
        final String[] sexOptions = {"Female", "Male"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Sex");
        builder.setItems(sexOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userSex = sexOptions[which];
                sexEditText.setText(userSex);
                saveSex();
            }
        });
        builder.show();
    }

    private void saveSex() {
        SharedPreferences.Editor editor = getSharedPreferences("ProfilePrefs", MODE_PRIVATE).edit();
        editor.putString("UserSex", userSex);
        editor.apply();
    }
    private void showLocationPickerDialog() {
        final String[] countries = {"Afghanistan", "Albania", "Algeria",
                "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
                "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia",
                "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados",
                "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
                "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil",
                "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria",
                "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",
                "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
                "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
                "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica",
                "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor",
                "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
                "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland",
                "France", "France Metropolitan", "French Guiana", "French Polynesia",
                "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany",
                "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam",
                "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                "Heard and Mc Donald Islands", "Holy See (Vatican City State)",
                "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia",
                "Iran (Islamic Republic of)", "Iraq", "Ireland", "Italy",
                "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, " +
                "Democratic People's Republic of", "Korea, Republic of", "Kuwait",
                "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon",
                "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania",
                "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of",
                "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
                "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte",
                "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco",
                "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
                "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia",
                "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island",
                "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau","Palestine", "Panama",
                "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland",
                "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation",
                "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
                "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles",
                "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)",
                "Slovenia", "Solomon Islands", "Somalia", "South Africa",
                "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka",
                "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname",
                "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland",
                "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan",
                "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga",
                "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
                "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
                "United Arab Emirates", "United Kingdom", "United States",
                "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
                "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)",
                "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen",
                "Yugoslavia", "Zambia", "Zimbabwe"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Location");
        builder.setItems(countries, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userLocation = countries[which];
                locationEditText.setText(userLocation);
                saveLocation();
            }
        });
        builder.show();
    }
    private void saveLocation() {
        SharedPreferences.Editor editor = getSharedPreferences("ProfilePrefs", MODE_PRIVATE).edit();
        editor.putString("UserLocation", userLocation);
        editor.apply();
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        userDob = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dobEditText.setText(userDob);
                        saveDob();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveDob() {
        SharedPreferences.Editor editor = getSharedPreferences("ProfilePrefs", MODE_PRIVATE).edit();
        editor.putString("UserDob", userDob);
        editor.apply();
    }
}

