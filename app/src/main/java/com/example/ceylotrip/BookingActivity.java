package com.example.ceylotrip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    // UI අංග ටික අඳුන්වලා දීම
    TextView tvBookingPackageName, tvTravelDate, tvAdultCount, tvChildCount, tvTotalPrice;
    Button btnAdultPlus, btnAdultMinus, btnChildPlus, btnChildMinus, btnConfirmBooking;
    CheckBox cbLocalGuide, cbTransport;

    // ගණනය කිරීම් සඳහා අවශ්‍ය Variables
    int basePrice = 0; // එක්කෙනෙක්ගේ මිල
    int adultCount = 1; // මුලින්ම වැඩිහිටියන් 1යි
    int childCount = 0; // මුලින්ම ළමයි 0යි

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // 1. XML එකේ තියෙන Views Java වලට සම්බන්ධ කිරීම
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

        // 2. DetailsActivity එකෙන් එවපු දත්ත ලබා ගැනීම
        String packageName = getIntent().getStringExtra("packageName");
        String priceString = getIntent().getStringExtra("packagePrice"); // උදා: "18,000"

        if (packageName != null) {
            tvBookingPackageName.setText(packageName);
        }

        // මිල ගණනය කරන්න පුළුවන් ඉලක්කමක් (Integer) බවට පත් කිරීම (කොමා අයින් කිරීම)
        if (priceString != null) {
            try {
                // "18,000" -> "18000" බවට පත් කරලා int එකක් කරනවා
                basePrice = Integer.parseInt(priceString.replace(",", ""));
            } catch (NumberFormatException e) {
                basePrice = 0;
            }
        }

        // මුල්ම මිල පෙන්වීම
        calculateTotal();

        // 3. Date Picker (දිනය තෝරන්න Calendar එකක් දීම)
        tvTravelDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // තේරුවට පස්සේ දිනය පෙන්වනවා
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        tvTravelDate.setText(date);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // 4. Adults (වැඩිහිටියන්) ගණන වෙනස් කිරීම
        btnAdultPlus.setOnClickListener(v -> {
            adultCount++;
            tvAdultCount.setText(String.valueOf(adultCount));
            calculateTotal(); // ගණන වෙනස් වුණාම මුළු මුදලත් හදනවා
        });

        btnAdultMinus.setOnClickListener(v -> {
            if (adultCount > 1) { // 1ට වඩා අඩුවෙන්න දෙන්නේ නෑ
                adultCount--;
                tvAdultCount.setText(String.valueOf(adultCount));
                calculateTotal();
            }
        });

        // 5. Children (ළමයින්) ගණන වෙනස් කිරීම
        btnChildPlus.setOnClickListener(v -> {
            childCount++;
            tvChildCount.setText(String.valueOf(childCount));
            calculateTotal();
        });

        btnChildMinus.setOnClickListener(v -> {
            if (childCount > 0) { // 0ට වඩා අඩුවෙන්න දෙන්නේ නෑ
                childCount--;
                tvChildCount.setText(String.valueOf(childCount));
                calculateTotal();
            }
        });

        // 6. Add-ons (අමතර දේවල්) Check කරද්දී මිල වෙනස් කිරීම
        cbLocalGuide.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());
        cbTransport.setOnCheckedChangeListener((buttonView, isChecked) -> calculateTotal());

        // 7. Confirm බොත්තම එබුවම
        btnConfirmBooking.setOnClickListener(v -> {
            if (tvTravelDate.getText().toString().equals("Select Date")) {
                Toast.makeText(BookingActivity.this, "Please select a travel date!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BookingActivity.this, "Booking Confirmed Successfully!", Toast.LENGTH_LONG).show();
                // මීළඟ පියවරේදී අපි මේ දත්ත Firebase Database එකට සේව් කරමු
            }
        });
    }

    // මුළු මුදල සජීවීව ගණනය කරන Function එක
    private void calculateTotal() {
        // වැඩිහිටියන්ට සම්පූර්ණ ගාණ
        int adultTotal = adultCount * basePrice;

        // ළමයින්ට ගාණෙන් භාගයයි (Half Price)
        int childTotal = childCount * (basePrice / 2);

        // Add-ons වල මිල (Check කරලා නම් විතරක් එකතු කරනවා)
        int addonsTotal = 0;
        if (cbLocalGuide.isChecked()) addonsTotal += 5000;
        if (cbTransport.isChecked()) addonsTotal += 10000;

        // සම්පූර්ණ එකතුව
        int finalTotal = adultTotal + childTotal + addonsTotal;

        // එකතුව පෙන්වීම
        tvTotalPrice.setText("Rs. " + finalTotal);
    }
}