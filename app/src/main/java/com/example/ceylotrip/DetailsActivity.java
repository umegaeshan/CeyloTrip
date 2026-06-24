package com.example.ceylotrip;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    ImageView ivDetailImage;
    TextView tvDetailTitle, tvDetailDuration, tvDetailPrice;
    Button btnBookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // 1. XML එකේ තියෙන Views ටික අඳුන්වලා දීම
        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDuration = findViewById(R.id.tvDetailDuration);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        btnBookNow = findViewById(R.id.btnBookNow);

        // 2. Adapter එකෙන් එවපු දත්ත (Intent Extras) ටික අල්ලගැනීම
        String title = getIntent().getStringExtra("title");
        String duration = getIntent().getStringExtra("duration");
        String price = getIntent().getStringExtra("price");
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // 3. ඒ දත්ත ටික අදාළ තැන් වලට දැමීම
        if (title != null) {
            tvDetailTitle.setText(title);
            tvDetailDuration.setText(duration);
            tvDetailPrice.setText("Rs. " + price + " per person");

            // Glide පාවිච්චි කරලා අදාළ පින්තූරය Load කිරීම
            Glide.with(this)
                    .load(imageUrl)
                    .into(ivDetailImage);
        }

        // Book Now බොත්තම එබුවම වෙන දේ (දැනට පොඩි මැසේජ් එකක් විතරක් පෙන්වමු)
        btnBookNow.setOnClickListener(v -> {
            Toast.makeText(DetailsActivity.this, "Booking form loading...", Toast.LENGTH_SHORT).show();
            // ඊළඟ පියවරේදී අපි මෙතනින් අපේ අලුත් Dynamic Booking Form එකට යන්න හදමු!
        });
    }
}