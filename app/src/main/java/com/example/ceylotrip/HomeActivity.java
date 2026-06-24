package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PackageAdapter adapter;
    List<PackageModel> packageList;

    EditText etSearch; // Search Bar එක අඳුන්වලා දීම
    ImageView ivHeroBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Search Bar එක සම්බන්ධ කිරීම
        etSearch = findViewById(R.id.etSearch);
        ivHeroBanner = findViewById(R.id.ivHeroBanner);

        // Hero Banner එකට පින්තූරයක් දැමීම
        Glide.with(this)
                .load("https://images.unsplash.com/photo-1546708973-c603a1523315?q=80&w=800")
                .into(ivHeroBanner);

        // RecyclerView එක සම්බන්ධ කිරීම
        recyclerView = findViewById(R.id.recyclerViewPackages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Packages දත්ත එකතු කිරීම
        packageList = new ArrayList<>();
        packageList.add(new PackageModel(
                "Galle Heritage & Sea",
                "2 Days / 1 Night",
                "18,000",
                "https://images.unsplash.com/photo-1588258524337-017e8840243e?q=80&w=600"
        ));
        packageList.add(new PackageModel(
                "Matara South Coast",
                "2 Days / 1 Night",
                "15,000",
                "https://images.unsplash.com/photo-1550100411-93d3950ff456?q=80&w=600"
        ));
        packageList.add(new PackageModel(
                "Nuwara Eliya Hill Escape",
                "3 Days / 2 Nights",
                "22,000",
                "https://images.unsplash.com/photo-1620023608240-6213fc817293?q=80&w=600"
        ));

        // මුලින්ම ඔක්කොම Data ටික RecyclerView එකට දීම
        adapter = new PackageAdapter(this, packageList);
        recyclerView.setAdapter(adapter);

        // ==========================================
        // Search ක්‍රියාකාරීත්වය එකතු කිරීම
        // ==========================================
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // අකුරු ටයිප් කරන ගමන් Filter Function එකට ඒ අකුරු යවනවා
                filter(s.toString());
            }
        });

        // ==========================================
        // Bottom Navigation Bar එක
        // ==========================================
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(HomeActivity.this, MyBookingsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    // අකුරු වලට ගැළපෙන පැකේජ් හොයන Function එක
    private void filter(String text) {
        List<PackageModel> filteredList = new ArrayList<>();

        for (PackageModel item : packageList) {
            // ටයිප් කරන අකුරු පැකේජ් එකේ නමේ තියෙනවද කියලා බලනවා (කැපිටල්/සිම්පල් ප්‍රශ්නයක් නැතිවෙන්න හදලා තියෙනවා)
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No packages found", Toast.LENGTH_SHORT).show();
        }

        // අලුතින් හැදුණු (Filter වුණු) ලිස්ට් එක Adapter එකට දීලා තිරයේ පෙන්වනවා
        adapter = new PackageAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
    }
}