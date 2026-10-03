package com.example.ceylotrip;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions; // අලුතින් එකතු කළ Import එක

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    EditText etProfileName, etProfileEmail;
    Button btnUpdateProfile, btnLogout;

    // Firebase Variables
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1. XML එකේ තියෙන Views සම්බන්ධ කිරීම
        etProfileName = findViewById(R.id.etProfileName);
        etProfileEmail = findViewById(R.id.etProfileEmail);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Firebase Initialize කිරීම
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // 2. Firebase එකෙන් ලොග් වුණු කෙනාගේ පරණ Data අරන් පෙන්වීම
        if (fAuth.getCurrentUser() != null) {
            String userID = fAuth.getCurrentUser().getUid();

            // Email එක Auth එකෙන්ම ගන්න පුළුවන් නිසා ඒක කෙලින්ම ගන්නවා
            String userEmail = fAuth.getCurrentUser().getEmail();
            if(userEmail != null) {
                etProfileEmail.setText(userEmail);
            }

            // Firestore එකෙන් නම ගන්නවා
            fStore.collection("Users").document(userID).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("fullName");
                    if (name != null) {
                        etProfileName.setText(name);
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(ProfileActivity.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }

        // 3. Update / Edit Profile බොත්තම ක්‍රියාත්මක කිරීම
        btnUpdateProfile.setOnClickListener(v -> {
            if (fAuth.getCurrentUser() == null) return;
            String userID = fAuth.getCurrentUser().getUid();

            if (!isEditing) {
                // Edit Mode එක On කරනවා
                isEditing = true;

                etProfileName.setEnabled(true);
                etProfileName.requestFocus();
                etProfileName.setBackgroundResource(android.R.drawable.edit_text);

                btnUpdateProfile.setText("Save Changes");
                btnUpdateProfile.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            } else {
                // Save කරනවා
                String updatedName = etProfileName.getText().toString().trim();

                if (updatedName.isEmpty()) {
                    etProfileName.setError("Name cannot be empty");
                    return;
                }

                // Database එකේ Save කරන්න දත්ත සෙට් කිරීම
                Map<String, Object> editedData = new HashMap<>();
                editedData.put("fullName", updatedName);
                // ඊමේල් එක Update කරන්න දෙන්නේ නැති නිසා ඒක යවන්නේ නෑ

                // Update වෙනුවට SetOptions.merge() පාවිච්චි කරනවා (Error එන්නේ නෑ)
                fStore.collection("Users").document(userID).set(editedData, SetOptions.merge())
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ProfileActivity.this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();

                            // Edit Mode එක Off කරනවා
                            isEditing = false;
                            etProfileName.setEnabled(false);
                            etProfileName.setBackgroundColor(Color.TRANSPARENT);

                            btnUpdateProfile.setText("Edit Profile");
                            btnUpdateProfile.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#009772")));
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProfileActivity.this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // 4. Logout වීම
        btnLogout.setOnClickListener(v -> {
            fAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 5. Bottom Navigation Bar එක හැසිරවීම
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationProfile);
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(ProfileActivity.this, MyBookingsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            else if (itemId == R.id.nav_explore) {
                startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }
}