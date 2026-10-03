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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText etSignUpName, etSignUpEmail, etSignUpPassword;
    Button btnSignUp;
    TextView tvGoToLogin;

    // Firebase සම්බන්ධ කරගැනීමට අවශ්‍ය variables
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // XML එකේ තියෙන UI අංග Java වලට සම්බන්ධ කිරීම
        etSignUpName = findViewById(R.id.etSignUpName);
        etSignUpEmail = findViewById(R.id.etSignUpEmail);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Firebase Initialize කිරීම
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Sign Up Button එක එබුවම වෙන දේ
        btnSignUp.setOnClickListener(v -> {
            String name = etSignUpName.getText().toString().trim();
            String email = etSignUpEmail.getText().toString().trim();
            String password = etSignUpPassword.getText().toString().trim();

            // හිස්තැන් තියෙනවද කියලා පරීක්ෂා කිරීම (Simple Validation)
            if (TextUtils.isEmpty(name)) {
                etSignUpName.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                etSignUpEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                etSignUpPassword.setError("Password must be at least 6 characters");
                return;
            }

            // Firebase Auth හරහා අලුත් ගිණුමක් හැදීම
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // ගිණුම සාර්ථකව හැදුණාම User ID එක ගන්නවා
                    String userID = fAuth.getCurrentUser().getUid();

                    // Database එකට යවන්න Data ටික Map එකකට (පැකට් එකකට වගේ) දාගන්නවා
                    Map<String, Object> user = new HashMap<>();
                    user.put("fullName", name);
                    user.put("email", email);

                    // Firestore එකේ "Users" කියන Collection එකට Data ටික සේව් කරනවා
                    fStore.collection("Users").document(userID).set(user).addOnSuccessListener(aVoid -> {
                        Toast.makeText(SignUpActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();

                        // සේව් වුණාට පස්සේ HomeActivity එකට යවනවා
                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // ආයේ Sign Up එකට Back වෙන්න බැරි වෙන්න
                    });
                } else {
                    // මොකක්හරි වැරැද්දක් වුණොත් Error එක පෙන්වනවා
                    Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // "Already have an account? Sign In" එක එබුවම Login එකට යනවා
        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}