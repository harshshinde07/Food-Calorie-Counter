package com.apps.harsh.foodcalorietracker.fragments;

import android.os.Bundle;
import com.apps.harsh.foodcalorietracker.R;

import androidx.appcompat.app.AppCompatActivity;



public class ProfileFragment extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
    }
}
