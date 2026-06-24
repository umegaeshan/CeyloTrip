package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        rvMyBookings = findViewById(R.id.rvMyBookings);
        rvMyBookings.setLayoutManager(new LinearLayoutManager(this));

        bookingList = new ArrayList<>();

        // 1. Adapter එක හදනකොට අලුතින් Click Listener එකත් පාස් කරනවා
        adapter = new BookingAdapter(this, bookingList, new BookingAdapter.OnBookingClickListener() {
            @Override
            public void onEditClick(BookingModel booking) {
                // Edit බොත්තම එබුවම BookingActivity එකට යනවා (Edit Mode එකෙන්)
                Intent intent = new Intent(MyBookingsActivity.this, BookingActivity.class);
                intent.putExtra("isEditMode", true); // මේක අලුතින් edit කරන්න යන බව කියන්න
                intent.putExtra("documentId", booking.getDocumentId()); // අදාළ බුකිං එකේ ID එක
                intent.putExtra("packageName", booking.getPackageName());
                // මිල වෙනස් කරලා තියෙන නිසා පරණ BasePrice එක ගන්න විදිහක් අපි ඊළඟට BookingActivity එකේදී හදමු
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(BookingModel booking, int position) {
                // මකන්න කලින් ඇත්තටම මකන්නද කියලා අහනවා (Confirmation Dialog)
                new AlertDialog.Builder(MyBookingsActivity.this)
                        .setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel this booking?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Yes දුන්නොත් Database එකෙන් මකන Function එකට යනවා
                            deleteBooking(booking.getDocumentId(), position);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        rvMyBookings.setAdapter(adapter);

        // Firebase Initialize කිරීම
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Database එකෙන් දත්ත ගැනීම අරඹනවා
        fetchMyBookings();

        // ==========================================
        // Bottom Navigation Bar එක සම්බන්ධ කිරීම
        // ==========================================
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationBookings);
        bottomNavigation.setSelectedItemId(R.id.nav_bookings);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(MyBookingsActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MyBookingsActivity.this, ProfileActivity.class));
                finish();
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

    private void fetchMyBookings() {
        if (fAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = fAuth.getCurrentUser().getUid();

        fStore.collection("Bookings")
                .whereEqualTo("userId", userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BookingModel booking = document.toObject(BookingModel.class);
                            // අලුත් කොටස: Database එකේ Document ID එක අරගෙන Model එකට දානවා (මකන්න සහ edit කරන්න මේක අත්‍යවශ්‍යයි)
                            booking.setDocumentId(document.getId());
                            bookingList.add(booking);
                        }

                        adapter.notifyDataSetChanged();

                        if (bookingList.isEmpty()) {
                            Toast.makeText(MyBookingsActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MyBookingsActivity.this, "Error getting data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 2. අලුත් කොටස: Database එකෙන් බුකිං එක මකා දැමීම
    private void deleteBooking(String documentId, int position) {
        if (documentId == null) return;

        fStore.collection("Bookings").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MyBookingsActivity.this, "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();

                    // Database එකෙන් මැකුවට පස්සේ අපේ ලැයිස්තුවෙනුත් අයින් කරලා තිරයෙන් මකනවා
                    bookingList.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyBookingsActivity.this, "Failed to cancel booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}