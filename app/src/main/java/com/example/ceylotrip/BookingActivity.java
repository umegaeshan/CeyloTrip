package com.example.ceylotrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// අලුතින් එකතු කරපු Firebase Imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    TextView tvBookingPackageName, tvTravelDate, tvAdultCount, tvChildCount, tvTotalPrice;
    Button btnAdultPlus, btnAdultMinus, btnChildPlus, btnChildMinus, btnConfirmBooking;
    CheckBox cbLocalGuide, cbTransport;

    int basePrice = 0;
    int adultCount = 1;
    int childCount = 0;
    int finalTotal = 0; // Database එකට යවන්න ලේසි වෙන්න මේක එළියට ගත්තා

    // Firebase Variables
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Firebase Initialize කිරීම
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // XML එකේ Views සම්බන්ධ කිරීම
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

        // DetailsActivity එකෙන් එවපු දත්ත ලබා ගැනීම
        String packageName = getIntent().getStringExtra("packageName");
        String priceString = getIntent().getStringExtra("packagePrice");

        if (packageName != null) {
            tvBookingPackageName.setText(packageName);
        }

        if (priceString != null) {
            try {
                basePrice = Integer.parseInt(priceString.replace(",", ""));
            } catch (NumberFormatException e) {
                basePrice = 0;
            }
        }

        calculateTotal();

        // Date Picker
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

        // Counters
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

        // Checkboxes
        cbLocalGuide.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());
        cbTransport.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());

        // ==========================================
        // 7. අලුත් කොටස: Database එකට සේව් කිරීම
        // ==========================================
        btnConfirmBooking.setOnClickListener(v -> {
            String selectedDate = tvTravelDate.getText().toString();

            if (selectedDate.equals("Select Date")) {
                Toast.makeText(BookingActivity.this, "Please select a travel date!", Toast.LENGTH_SHORT).show();
                return; // දිනයක් දීලා නැත්නම් මෙතනින් නවතිනවා
            }

            // ලොග් වෙලා ඉන්න කෙනාගේ User ID එක ගන්නවා
            String userID = fAuth.getCurrentUser().getUid();

            // සේව් කරන්න ඕන දත්ත ටික Map එකකට (පැකට් එකකට) දානවා
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("userId", userID);
            bookingData.put("packageName", packageName);
            bookingData.put("travelDate", selectedDate);
            bookingData.put("adults", adultCount);
            bookingData.put("children", childCount);
            bookingData.put("hasLocalGuide", cbLocalGuide.isChecked());
            bookingData.put("hasTransport", cbTransport.isChecked());
            bookingData.put("totalPrice", finalTotal);
            bookingData.put("status", "Pending"); // මුලින්ම Pending විදිහට සේව් කරමු

            // "Bookings" කියන අලුත් Collection එකට මේක සේව් කරනවා
            fStore.collection("Bookings").add(bookingData).addOnSuccessListener(documentReference -> {
                // සේව් වුණාම පෙන්වන මැසේජ් එක
                Toast.makeText(BookingActivity.this, "Booking Successful!", Toast.LENGTH_LONG).show();

                // සේව් වුණාට පස්සේ ආපහු Home Screen එකට යවනවා
                Intent intent = new Intent(BookingActivity.this, HomeActivity.class);
                // අලුත් Activity එකක් අරින්නේ නැතුව, පරණ Home එකටම යන්න මේ Flags දානවා
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }).addOnFailureListener(e -> {
                Toast.makeText(BookingActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        });
    }

    private void calculateTotal() {
        int adultTotal = adultCount * basePrice;
        int childTotal = childCount * (basePrice / 2);

        int addonsTotal = 0;
        if (cbLocalGuide.isChecked()) addonsTotal += 5000;
        if (cbTransport.isChecked()) addonsTotal += 10000;

        finalTotal = adultTotal + childTotal + addonsTotal; // අලුතින් හදපු variable එකට දානවා
        tvTotalPrice.setText("Rs. " + finalTotal);
    }
}