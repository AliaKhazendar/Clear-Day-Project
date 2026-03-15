package com.example.cleardayapplication.ui.activitys;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cleardayapplication.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.splashStartButton.setOnClickListener(v ->{
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
            finish();
        });

            SharedPreferences prefs = getSharedPreferences("clear_day_prefs", MODE_PRIVATE);
            boolean isFirstTime = prefs.getBoolean("isFirstTime", true);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (isLoggedIn) {
                // المستخدم سجل الدخول مسبقاً -> اذهب مباشرة
//                startActivity(new Intent(this, HomeActivity.class)); // أو AccountingActivity حسب تصميمك
//                finish();
                return; // مهم نرجع قبل أي setContentView
            } else if (isFirstTime) {
                // أول مرة -> اذهب إلى Onboarding
                startActivity(new Intent(this, OnBoardingActivity.class));
                finish();
                return;
            }

            // إذا وصل هنا، يعني ليس أول مرة ولم يسجل الدخول
            startActivity(new Intent(this, AccountingActivity.class));
            finish();
        }
}