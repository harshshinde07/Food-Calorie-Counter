package com.apps.harsh.foodcalorietracker.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.harsh.foodcalorietracker.R;
import com.apps.harsh.foodcalorietracker.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileFragment extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    TextView name, age, blood, height, weight, gender, goal, profileName;
    String nameString, ageString, bloodString, heightString, weightString, genderString, goalString;
    ImageView edit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        edit = findViewById(R.id.drop_down_option_menu);
        profileName = findViewById(R.id.user_profile_name);

        name = findViewById(R.id.txtName);
        age = findViewById(R.id.textAge);
        blood = findViewById(R.id.textBGroup);
        height = findViewById(R.id.textHeight);
        weight = findViewById(R.id.textWeight);
        gender = findViewById(R.id.TextGender);
        goal = findViewById(R.id.textGoal);

        nameString = name.getText().toString();
        ageString = age.getText().toString();
        bloodString = blood.getText().toString();
        heightString = height.getText().toString();
        weightString = weight.getText().toString();
        genderString = gender.getText().toString();
        goalString = goal.getText().toString();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredName = prefs.getString("prefName", null);
        String restoredAge = prefs.getString("prefAge", null);
        String restoredGender = prefs.getString("prefGender", null);
        String restoredBlood = prefs.getString("prefBlood", null);
        String restoredWeight = prefs.getString("prefWeight", null);
        String restoredHeight = prefs.getString("prefHeight", null);
        String restoredGoal = prefs.getString("prefGoal", null);
        if (restoredName != null) {
            name.setText(nameString.concat(restoredName));
            profileName.setText(restoredName);
        }
        if (restoredAge != null) {
            age.setText(ageString.concat(restoredAge));
        }
        if (restoredGender != null) {
            gender.setText(genderString.concat(restoredGender));
        }
        if (restoredBlood != null) {
            blood.setText(bloodString.concat(restoredBlood));
        }
        if (restoredWeight != null) {
            weight.setText(weightString.concat(restoredWeight));
        }
        if (restoredHeight != null) {
            height.setText(heightString.concat(restoredHeight));
        }
        if (restoredGoal != null) {
            goal.setText(goalString.concat(restoredGoal));
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileFragment.this, WelcomeActivity.class));
            }
        });
    }
}
