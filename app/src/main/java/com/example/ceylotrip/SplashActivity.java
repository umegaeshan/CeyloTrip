package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // SplashActivity.java ඇතුළේ වෙනස් වෙන්න ඕන කොටස

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // LoginActivity වෙනුවට OnboardingActivity එකට යවනවා
                Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}