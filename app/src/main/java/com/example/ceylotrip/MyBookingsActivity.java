package com.example.ceylotrip;

import android.content.Intent; // Intent සඳහා අලුතින් එකතු කළ Import එක
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    RecyclerView rvMyBookings;
    BookingAdapter adapter;
    List<BookingModel> bookingList;

    // Firebase Variables
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        // 1. XML එකේ තියෙන RecyclerView එක සම්බන්ධ කිරීම
        rvMyBookings = findViewById(R.id.rvMyBookings);
        rvMyBookings.setLayoutManager(new LinearLayoutManager(this));

        // ලිස්ට් එක සහ Adapter එක හැදීම
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(this, bookingList);
        rvMyBookings.setAdapter(adapter);

        // 2. Firebase Initialize කිරීම
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // 3. Database එකෙන් දත්ත ගැනීම අරඹනවා
        fetchMyBookings();

        // ==========================================
        // Bottom Navigation Bar එක සම්බන්ධ කිරීම
        // ==========================================
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationBookings);

        // මේ පිටුවට ආවම "Bookings" අයිකන් එක Select වෙලා තියෙන්න ඕනේ
        bottomNavigation.setSelectedItemId(R.id.nav_bookings);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Home අයිකන් එක එබුවම ආපහු HomeActivity එකට යනවා
                startActivity(new Intent(MyBookingsActivity.this, HomeActivity.class));
                finish(); // මේ පිටුව වහනවා ආපහු Back කරන්න බැරි වෙන්න
                return true;
            } else if (itemId == R.id.nav_bookings) {
                // දැනටමත් ඉන්නේ Bookings පිටුවේ නිසා මුකුත් කරන්නේ නෑ
                return true;
            }

            return false;
        });
    }

    private void fetchMyBookings() {
        // දැනට ලොග් වෙලා ඉන්න කෙනාගේ User ID එක ගන්නවා
        if (fAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = fAuth.getCurrentUser().getUid();

        // "Bookings" Collection එකෙන් මේ User ID එකට අදාළ දත්ත විතරක් හොයනවා
        fStore.collection("Bookings")
                .whereEqualTo("userId", userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear(); // පරණ දත්ත තියෙනවා නම් මකනවා

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Database එකේ තියෙන දත්ත BookingModel එකට දාගන්නවා
                            BookingModel booking = document.toObject(BookingModel.class);
                            bookingList.add(booking);
                        }

                        // අලුත් දත්ත ආවා කියලා Adapter එකට කියනවා (එතකොට තිරයේ පෙනෙනවා)
                        adapter.notifyDataSetChanged();

                        // බුකිං මුකුත් නැත්නම් පොඩි මැසේජ් එකක් දෙනවා
                        if (bookingList.isEmpty()) {
                            Toast.makeText(MyBookingsActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // Error එකක් ආවොත් පෙන්වනවා
                        Toast.makeText(MyBookingsActivity.this, "Error getting data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}