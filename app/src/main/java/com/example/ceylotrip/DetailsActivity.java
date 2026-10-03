package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDuration = findViewById(R.id.tvDetailDuration);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        btnBookNow = findViewById(R.id.btnBookNow);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String duration = intent.getStringExtra("duration");
            String price = intent.getStringExtra("price");
            String imageUrl = intent.getStringExtra("imageUrl");

            if (title != null) tvDetailTitle.setText(title);
            if (duration != null) tvDetailDuration.setText(duration);
            if (price != null) tvDetailPrice.setText("Rs. " + price + " per person");

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .centerCrop() // පින්තූරය කොටුවට හරියටම ගැලපෙන්න Load කරයි
                        .into(ivDetailImage);
            }

            btnBookNow.setOnClickListener(v -> {
                Intent bookingIntent = new Intent(DetailsActivity.this, BookingActivity.class);
                bookingIntent.putExtra("packageName", title);
                bookingIntent.putExtra("packagePrice", price);
                startActivity(bookingIntent);
            });
        }
    }
}