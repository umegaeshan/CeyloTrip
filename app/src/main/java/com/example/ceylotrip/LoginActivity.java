package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // XML එකේ තියෙන Button එක අඳුන්වලා දෙනවා
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // XML එකේ Button එකේ ID එක (btnLogin) Java වලට සම්බන්ධ කරනවා
        btnLogin = findViewById(R.id.btnLogin);

        // Button එක Click කළාම වෙන දේ ලියනවා
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // සරලවම HomeActivity එකට යවනවා
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}