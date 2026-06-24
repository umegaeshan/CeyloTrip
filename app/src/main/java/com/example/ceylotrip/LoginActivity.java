package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvSignUp;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Initialize කිරීම
        fAuth = FirebaseAuth.getInstance();

        // XML එකේ තියෙන අංග Java වලට සම්බන්ධ කිරීම
        etEmail = findViewById(R.id.etName); // සටහන: ඔයා XML එකේ මේකට etEmail කියලා නම දුන්නා නම්, මෙතනත් R.id.etEmail කියලා වෙනස් කරන්න.
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Login Button එක Click කළාම වෙන දේ
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // හිස්තැන් තියෙනවද කියලා පරීක්ෂා කිරීම
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is required");
                return;
            }

            // Firebase හරහා ගිණුමට ලොග් වීම (Authentication)
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // ආයේ Login එකට Back වෙන්න බැරි වෙන්න
                } else {
                    // පාස්වර්ඩ් එක හරි ඊමේල් එක හරි වැරදි නම් පෙන්වන පණිවිඩය
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Sign Up Text එක එබුවම Sign Up පිටුවට යැවීම
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}