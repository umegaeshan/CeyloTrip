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
    ImageView ivHeroBanner;
    EditText etSearch; // එකතු කළා

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ivHeroBanner = findViewById(R.id.ivHeroBanner);
        recyclerView = findViewById(R.id.recyclerViewPackages);
        etSearch = findViewById(R.id.etSearch); // ID සම්බන්ධ කළා

        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRk-oaWYJWCqjr8D_BQ1p4eA13bIF4C2G74PwH2mLAfwA&s")
                .centerCrop()
                .into(ivHeroBanner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        packageList = new ArrayList<>();
        packageList.add(new PackageModel("Galle Heritage & Sea", "2 Days / 1 Night", "18,000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT63rYavV1gDd6ZrJDPKV2suFsCwiHAcL6IJRaMqDQLFw&s=10"));
        packageList.add(new PackageModel("Matara South Coast", "2 Days / 1 Night", "15,000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqLnjnI8YbRHmoBL7UfmIhXpYVlTOA9IQTXYU7RC-JHA&s=10"));
        packageList.add(new PackageModel("Nuwara Eliya Hill", "3 Days / 2 Nights", "22,000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbPiUhPavt2lc2cxyNWZ4vmc0xesrJtxTg7m-FP5sHKQ&s=10"));

        adapter = new PackageAdapter(this, packageList);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_bookings) {
                startActivity(new Intent(HomeActivity.this, MyBookingsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_explore) {
                startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void filter(String text) {
        List<PackageModel> filteredList = new ArrayList<>();
        for (PackageModel item : packageList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No packages found", Toast.LENGTH_SHORT).show();
        }
        adapter = new PackageAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
    }
}