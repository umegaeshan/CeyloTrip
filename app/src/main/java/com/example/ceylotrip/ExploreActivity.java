package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {
    RecyclerView rvExplore;
    EditText etExploreSearch;
    PlaceAdapter adapter;
    List<PlaceModel> placeList;
    FirebaseFirestore fStore; // Firebase එකතු කළා

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        etExploreSearch = findViewById(R.id.etExploreSearch);
        rvExplore = findViewById(R.id.rvExplore);
        rvExplore.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        placeList = new ArrayList<>();
        adapter = new PlaceAdapter(this, placeList);
        rvExplore.setAdapter(adapter);

        // Database එකෙන් ඔක්කොම පැකේජ් ගැනීම
        loadAllPackagesFromDatabase();

        // Search Logic
        etExploreSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                List<PlaceModel> filtered = new ArrayList<>();
                for (PlaceModel p : placeList) {
                    if (p.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        filtered.add(p);
                    }
                }
                adapter = new PlaceAdapter(ExploreActivity.this, filtered);
                rvExplore.setAdapter(adapter);
            }
        });

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationExplore);
        bottomNavigation.setSelectedItemId(R.id.nav_explore);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) { startActivity(new Intent(this, HomeActivity.class)); finish(); return true; }
            if (id == R.id.nav_bookings) { startActivity(new Intent(this, MyBookingsActivity.class)); finish(); return true; }
            if (id == R.id.nav_profile) { startActivity(new Intent(this, ProfileActivity.class)); finish(); return true; }
            return false;
        });
    }

    private void loadAllPackagesFromDatabase() {
        // "Packages" කියන Collection එකෙන් ඔක්කොම දත්ත ගන්නවා
        fStore.collection("Packages").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                placeList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String duration = document.getString("duration");
                    String price = document.getString("price");
                    String imageUrl = document.getString("imageUrl");

                    if (title != null) {
                        placeList.add(new PlaceModel(title, duration, imageUrl, price));
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(ExploreActivity.this, "Failed to load database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}