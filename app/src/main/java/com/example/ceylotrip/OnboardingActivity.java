package com.example.ceylotrip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    TextView tvSkip, tvTitle, tvDescription;
    ImageView ivOnboarding;
    Button btnContinue;

    int currentScreen = 0; // මුල්ම තිරයේ අංකය 0

    // පෙන්වන්න ඕන විස්තර ලැයිස්තුගත කිරීම
    String[] titles = {
            "Explore Sri Lanka",
            "Easy & Fast Bookings",
            "Enjoy Your Trip"
    };

    String[] descriptions = {
            "Discover the most beautiful destinations and hidden gems around the island.",
            "Book your favorite travel packages easily with just a few taps.",
            "Experience the journey of a lifetime with our trusted CeyloTrip guides."
    };

    // Drawable folder එකට දාපු පින්තූර 3 (img_1, img_2, img_3 විදිහට දාන්න)
    // දැනට රතු ඉරක් පෙන්නුවොත් පින්තූර ටික drawable එකට දැම්මම ඒක හරි යනවා.
    int[] images = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // XML එකේ තියෙන ඒවා Java වලට සම්බන්ධ කිරීම
        tvSkip = findViewById(R.id.tvSkip);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        ivOnboarding = findViewById(R.id.ivOnboarding);
        btnContinue = findViewById(R.id.btnContinue);

        // මුල්ම තිරයේ විස්තර පෙන්වීම
        updateScreen(currentScreen);

        // Continue Button එක එබුවම වෙන දේ
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentScreen < titles.length - 1) {
                    currentScreen++; // ඊළඟ තිරයට අංකය වැඩි කරනවා
                    updateScreen(currentScreen);
                } else {
                    // අන්තිම තිරය නම් Login පිටුවට යනවා
                    goToLogin();
                }
            }
        });

        // Skip එබුවම කෙලින්ම Login පිටුවට යනවා
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    // තිරය update කරන function එක
    private void updateScreen(int index) {
        tvTitle.setText(titles[index]);
        tvDescription.setText(descriptions[index]);
        ivOnboarding.setImageResource(images[index]);

        // අන්තිම තිරයේදී Button එකේ නම වෙනස් කරනවා
        if (index == titles.length - 1) {
            btnContinue.setText("Get Started");
        } else {
            btnContinue.setText("Continue");
        }
    }

    // Login පිටුවට යන function එක
    private void goToLogin() {
        Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // ආයේ මේ පිටුවට එන්න බැරි වෙන්න මේක close කරනවා
    }
}