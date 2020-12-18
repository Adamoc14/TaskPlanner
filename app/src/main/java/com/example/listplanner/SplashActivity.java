// Package and Library Imports
package com.example.listplanner;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

// Class Declaration
public class SplashActivity extends AppCompatActivity {

    // Global Variable Declarations and function Definitions
    private int splash_screen_timer = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //  Setting up my handler to run and start my main activity after timer finishes
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //  Create an intent to configure and start up the main activity
                Intent startUpIntent = new Intent(SplashActivity.this , MainActivity.class);
                startActivity(startUpIntent);
                finish();
            }
        } , splash_screen_timer);
    }
}