package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent iNext;

                    iNext = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(iNext); // Remove Splash Screen and Start Home Activity

//                startActivity(iHome);
                finish();   // Finishes the activity to remove from back stack
            }
        }, 100);
    }
}