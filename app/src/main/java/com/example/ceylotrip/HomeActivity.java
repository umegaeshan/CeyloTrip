package com.example.ceylotrip;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PackageAdapter adapter;
    List<PackageModel> packageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewPackages);

        // ලිස්ට් එක වමේ ඉඳන් දකුණට (Horizontal) යන්න සෙට් කිරීම
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Packages ටික එකතු කිරීම (වෙබ් එකේ තිබුණා වගේම)
        packageList = new ArrayList<>();
        packageList.add(new PackageModel(
                "Galle Heritage & Sea",
                "2 Days / 1 Night",
                "18,000",
                "https://images.unsplash.com/photo-1588258524337-017e8840243e?q=80&w=600" // ගාල්ලේ පින්තූරයක්
        ));
        packageList.add(new PackageModel(
                "Matara South Coast",
                "2 Days / 1 Night",
                "15,000",
                "https://images.unsplash.com/photo-1550100411-93d3950ff456?q=80&w=600" // මුහුදු වෙරළක පින්තූරයක්
        ));
        packageList.add(new PackageModel(
                "Nuwara Eliya Hill Escape",
                "3 Days / 2 Nights",
                "22,000",
                "https://images.unsplash.com/photo-1620023608240-6213fc817293?q=80&w=600" // කෝච්චියක පින්තූරයක්
        ));

        // Adapter එක හරහා Data ටික RecyclerView එකට දීම
        adapter = new PackageAdapter(this, packageList);
        recyclerView.setAdapter(adapter);
    }
}