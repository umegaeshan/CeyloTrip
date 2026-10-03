package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
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
    FirebaseFirestore fStore;

    TextView tvUserName;
    FirebaseAuth fAuth;

    // අලුතින් එකතු කළ Variables (Slideshow එක සඳහා)
    String[] heroImages = {
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2f1-eeflZ8kmFiI642_iAuH19E-2wGKYwu4ULFbkfTQ&s=10",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_uncaOQzjPz7s4FIJEeZTs2naGwDXdxHZMyvMggEctQ&s=10",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWcueecmk_i_HvNQVw2YomAgXCMaPlwbSFQujVKjBqsw&s=10"
    };
    int currentImageIndex = 0;
    Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ivHeroBanner = findViewById(R.id.ivHeroBanner);
        recyclerView = findViewById(R.id.recyclerViewPackages);
        etSearch = findViewById(R.id.etSearch);

        // 2. අලුත් View එක සහ Auth එක සම්බන්ධ කිරීම
        tvUserName = findViewById(R.id.tvUserName);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // 3. Database එකෙන් ලොග් වෙලා ඉන්න කෙනාගේ නම ගැනීම
        if (fAuth.getCurrentUser() != null) {
            String userID = fAuth.getCurrentUser().getUid();

            fStore.collection("Users").document(userID).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String fullName = documentSnapshot.getString("fullName");
                    if (fullName != null) {
                        // සම්පූර්ණ නමෙන් මුල් නම (First Name) පමණක් වෙන් කර ගැනීම
                        String firstName = fullName.split(" ")[0];
                        tvUserName.setText("Hello, " + firstName + "!");
                    }
                }
            });
        }

        // ස්වයංක්‍රීයව පින්තූර මාරු කරන (Slideshow) කේතය
        Runnable imageUpdater = new Runnable() {
            @Override
            public void run() {
                if (isDestroyed()) return; // Activity එක close කරලා නම් error එන එක වළක්වයි

                Glide.with(HomeActivity.this)
                        .load(heroImages[currentImageIndex])
                        .transition(DrawableTransitionOptions.withCrossFade(1000)) // තත්පර 1ක Fade ඇනිමේෂන් එක
                        .centerCrop()
                        .into(ivHeroBanner);

                // ඊළඟ පින්තූරයට යෑම (අවසන් එකට ආවම ආයෙත් මුලට යයි)
                currentImageIndex = (currentImageIndex + 1) % heroImages.length;

                // තත්පර 3කට වරක් පින්තූරය මාරු වේ
                sliderHandler.postDelayed(this, 3000);
            }
        };

        // Slideshow එක ආරම්භ කිරීම
        sliderHandler.post(imageUpdater);

        // RecyclerView සැකසීම
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        packageList = new ArrayList<>();
        adapter = new PackageAdapter(this, packageList);
        recyclerView.setAdapter(adapter);

        // Database එකෙන් දත්ත ලබා ගැනීම
        loadPopularPackagesFromDatabase();

        // 1. Search Filter Logic එක
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        // 2. Category බොත්තම් සම්බන්ධ කිරීම
        View btnBeaches = findViewById(R.id.btnCategoryBeaches);
        View btnWildlife = findViewById(R.id.btnCategoryWildlife);
        View btnHeritage = findViewById(R.id.btnCategoryHeritage);

        if (btnBeaches != null) btnBeaches.setOnClickListener(v -> filterByCategory("Beaches"));
        if (btnWildlife != null) btnWildlife.setOnClickListener(v -> filterByCategory("Wildlife"));
        if (btnHeritage != null) btnHeritage.setOnClickListener(v -> filterByCategory("Heritage"));

        // 3. Bottom Navigation එක
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

    // Firebase එකෙන් දත්ත ඇදලා ගන්නා Method එක
    private void loadPopularPackagesFromDatabase() {
        // Home එකට පෙන්වන්න පැකේජ් 5ක් පමණක් ගනිමු
        fStore.collection("Packages").limit(5).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                packageList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("title");
                    String duration = document.getString("duration");
                    String price = document.getString("price");
                    String imageUrl = document.getString("imageUrl");

                    if (title != null) {
                        packageList.add(new PackageModel(title, duration, price, imageUrl));
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(HomeActivity.this, "Failed to load database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Search එකේ අකුරු ටයිප් කරද්දි Filter වන Method එක
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

        if (adapter != null) {
            adapter.setFilteredList(filteredList);
        }
    }

    // Category Click කරද්දි Filter වන Method එක
    private void filterByCategory(String category) {
        List<PackageModel> filteredList = new ArrayList<>();

        for (PackageModel item : packageList) {
            String title = item.getTitle().toLowerCase();

            // අපි Firebase එකට දාපු දත්ත වල නම් (Keywords) අනුව තෝරනවා
            if (category.equals("Beaches") && (title.contains("beach") || title.contains("sea") || title.contains("bay") || title.contains("mirissa") || title.contains("trincomalee"))) {
                filteredList.add(item);
            } else if (category.equals("Wildlife") && (title.contains("safari") || title.contains("whale") || title.contains("yala") || title.contains("wild"))) {
                filteredList.add(item);
            } else if (category.equals("Heritage") && (title.contains("heritage") || title.contains("fortress") || title.contains("temple") || title.contains("ancient") || title.contains("fort") || title.contains("sigiriya") || title.contains("kandy") || title.contains("anuradhapura"))) {
                filteredList.add(item);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No packages found for " + category, Toast.LENGTH_SHORT).show();
        }

        if (adapter != null) {
            adapter.setFilteredList(filteredList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity එක close වෙද්දී Handler එක නතර කිරීම
        if (sliderHandler != null) {
            sliderHandler.removeCallbacksAndMessages(null);
        }
    }
}