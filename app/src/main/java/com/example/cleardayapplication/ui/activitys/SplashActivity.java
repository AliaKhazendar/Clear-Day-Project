package com.example.cleardayapplication.ui.activitys;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cleardayapplication.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
<<<<<<< HEAD
=======
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
>>>>>>> cc4551c6556167d911c6d7e407d26505328458a1
        binding.splashStartButton.setOnClickListener(v ->{
                checkUserStatus();
        });
<<<<<<< HEAD

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
=======
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
>>>>>>> cc4551c6556167d911c6d7e407d26505328458a1
}