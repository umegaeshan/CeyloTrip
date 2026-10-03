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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PackageAdapter adapter;
    List<PackageModel> packageList;
    ImageView ivHeroBanner;
    EditText etSearch;
    FirebaseFirestore fStore; // Firebase එකතු කළා

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ivHeroBanner = findViewById(R.id.ivHeroBanner);
        recyclerView = findViewById(R.id.recyclerViewPackages);
        etSearch = findViewById(R.id.etSearch);
        fStore = FirebaseFirestore.getInstance(); // Firebase Initialize කිරීම

        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRk-oaWYJWCqjr8D_BQ1p4eA13bIF4C2G74PwH2mLAfwA&s")
                .centerCrop()
                .into(ivHeroBanner);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        packageList = new ArrayList<>();
        adapter = new PackageAdapter(this, packageList);
        recyclerView.setAdapter(adapter);

        // Database එකෙන් Popular පැකේජ් 3ක් පමණක් ගැනීම
        loadPopularPackagesFromDatabase();

        // Search Filter Logic එක
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

    private void loadPopularPackagesFromDatabase() {
        // "Packages" කියන Collection එකෙන් පැකේජ් 3ක් විතරක් Home එකට ගන්නවා
        fStore.collection("Packages").limit(3).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                packageList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String duration = document.getString("duration");
                    String price = document.getString("price");
                    String imageUrl = document.getString("imageUrl");

                    if (title != null) { // Error එන එක වළක්වන්න
                        packageList.add(new PackageModel(title, duration, price, imageUrl));
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(HomeActivity.this, "Failed to load database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        List<PackageModel> filteredList = new ArrayList<>();
        for (PackageModel item : packageList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty() && !text.isEmpty()) {
            Toast.makeText(this, "No packages found", Toast.LENGTH_SHORT).show();
        }

        // අර අපි අලුතින් හැදූ method එකට දත්ත යැවීම
        if (adapter != null) {
            adapter.setFilteredList(filteredList);
        }
    }
}