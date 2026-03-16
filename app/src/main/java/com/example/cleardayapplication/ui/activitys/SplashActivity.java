package com.example.cleardayapplication.ui.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        binding.splashStartButton.setOnClickListener(v ->{
                checkUserStatus();
        });
    }
    private void checkUserStatus() {
        // 1. التحقق هل هذه أول مرة يفتح فيها التطبيق (لإظهار الـ OnBoarding)
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            // إذا كانت أول مرة، نذهب لشاشة الترحيب
            startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
        } else {
            // 2. إذا لم تكن المرة الأولى، نتحقق من تسجيل الدخول عبر Firebase
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // المستخدم مسجل دخوله مسبقاً -> نذهب للرئيسية مباشرة
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            } else {
                // المستخدم غير مسجل -> نذهب لشاشة الحسابات
                startActivity(new Intent(SplashActivity.this, AccountingActivity.class));
            }
        }
        finish();
    }
}