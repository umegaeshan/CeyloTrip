package com.example.ceylotrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    ImageView ivBookingImage;
    TextView tvBookingPackageName, tvTravelDate, tvAdultCount, tvChildCount, tvTotalPrice;
    Button btnAdultPlus, btnAdultMinus, btnChildPlus, btnChildMinus, btnConfirmBooking;
    CheckBox cbLocalGuide, cbTransport;

    int basePrice = 0;
    int adultCount = 1;
    int childCount = 0;
    int finalTotal = 0;

    boolean isEditMode = false;
    String documentId = "";
    String imageUrl = "";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        ivBookingImage = findViewById(R.id.ivBookingImage);
        tvBookingPackageName = findViewById(R.id.tvBookingPackageName);
        tvTravelDate = findViewById(R.id.tvTravelDate);
        tvAdultCount = findViewById(R.id.tvAdultCount);
        tvChildCount = findViewById(R.id.tvChildCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        btnAdultPlus = findViewById(R.id.btnAdultPlus);
        btnAdultMinus = findViewById(R.id.btnAdultMinus);
        btnChildPlus = findViewById(R.id.btnChildPlus);
        btnChildMinus = findViewById(R.id.btnChildMinus);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        cbLocalGuide = findViewById(R.id.cbLocalGuide);
        cbTransport = findViewById(R.id.cbTransport);

        // පෙර පිටුවෙන් දත්ත ලබා ගැනීම
        String packageName = getIntent().getStringExtra("packageName");
        String priceString = getIntent().getStringExtra("packagePrice");
        imageUrl = getIntent().getStringExtra("imageUrl");

        isEditMode = getIntent().getBooleanExtra("isEditMode", false);
        documentId = getIntent().getStringExtra("documentId");

        if (packageName != null) {
            tvBookingPackageName.setText(packageName);
        }

        if (priceString != null) {
            try {
                basePrice = Integer.parseInt(priceString.replace(",", "").replace("Rs. ", "").trim());
            } catch (NumberFormatException e) {
                basePrice = 0;
            }
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .centerCrop()
                    .into(ivBookingImage);
        }

        // Edit කරනවා නම් බොත්තමේ නම වෙනස් කිරීම
        if (isEditMode) {
            btnConfirmBooking.setText("Update Booking");
        }

        calculateTotal();

        tvTravelDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        tvTravelDate.setText(date);
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnAdultPlus.setOnClickListener(v -> {
            adultCount++;
            tvAdultCount.setText(String.valueOf(adultCount));
            calculateTotal();
        });

        btnAdultMinus.setOnClickListener(v -> {
            if (adultCount > 1) {
                adultCount--;
                tvAdultCount.setText(String.valueOf(adultCount));
                calculateTotal();
            }
        });

        btnChildPlus.setOnClickListener(v -> {
            childCount++;
            tvChildCount.setText(String.valueOf(childCount));
            calculateTotal();
        });

        btnChildMinus.setOnClickListener(v -> {
            if (childCount > 0) {
                childCount--;
                tvChildCount.setText(String.valueOf(childCount));
                calculateTotal();
            }
        });

        cbLocalGuide.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());
        cbTransport.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());

        btnConfirmBooking.setOnClickListener(v -> {
            String selectedDate = tvTravelDate.getText().toString();

            if (selectedDate.equals("Select Date")) {
                Toast.makeText(BookingActivity.this, "Please select a travel date!", Toast.LENGTH_SHORT).show();
                return;
            }

            String userID = fAuth.getCurrentUser().getUid();

            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("userId", userID);
            bookingData.put("packageName", packageName);
            bookingData.put("travelDate", selectedDate);
            bookingData.put("adults", adultCount);
            bookingData.put("children", childCount);
            bookingData.put("hasLocalGuide", cbLocalGuide.isChecked());
            bookingData.put("hasTransport", cbTransport.isChecked());
            bookingData.put("totalPrice", finalTotal);
            bookingData.put("status", "Pending");

            // Image එක සහ Base Price එක Database එකට සේව් කිරීම (Edit කරන්න මේවා අත්‍යවශ්‍යයි)
            bookingData.put("imageUrl", imageUrl);
            bookingData.put("basePrice", basePrice);

            if (isEditMode && documentId != null && !documentId.isEmpty()) {
                // Edit Mode - පරණ දත්ත Update කිරීම
                fStore.collection("Bookings").document(documentId).update(bookingData).addOnSuccessListener(aVoid -> {
                    Toast.makeText(BookingActivity.this, "Booking Updated Successfully!", Toast.LENGTH_LONG).show();
                    goToHome();
                }).addOnFailureListener(e -> {
                    Toast.makeText(BookingActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                // New Booking - අලුතින් සේව් කිරීම
                fStore.collection("Bookings").add(bookingData).addOnSuccessListener(documentReference -> {
                    Toast.makeText(BookingActivity.this, "Booking Successful!", Toast.LENGTH_LONG).show();
                    goToHome();
                }).addOnFailureListener(e -> {
                    Toast.makeText(BookingActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void calculateTotal() {
        int adultTotal = adultCount * basePrice;
        int childTotal = childCount * (basePrice / 2);

        int addonsTotal = 0;
        if (cbLocalGuide.isChecked()) addonsTotal += 5000;
        if (cbTransport.isChecked()) addonsTotal += 10000;

        finalTotal = adultTotal + childTotal + addonsTotal;
        tvTotalPrice.setText("Rs. " + finalTotal);
    }

    private void goToHome() {
        Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}