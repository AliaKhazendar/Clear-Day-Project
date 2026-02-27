package com.example.cleardayapplication.ui.activitys;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ActivityAccountingBinding;
import com.example.cleardayapplication.ui.fragments.account.SingInFragment;
import com.example.cleardayapplication.ui.fragments.account.SingUpFragment;
import com.google.firebase.auth.FirebaseAuth;

public class AccountingActivity extends AppCompatActivity {

    private ActivityAccountingBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAccountingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        // التحقق من حالة المستخدم (هل هو مسجل الدخول؟)
        if (mAuth.getCurrentUser() != null) {
            // إذا كان المستخدم مسجل الدخول، اعرض شاشة تسجيل الدخول (SingInFragment)
            showFragment(new SingInFragment());
        } else {
            // إذا لم يكن لدى المستخدم حساب، اعرض شاشة التسجيل (SingUpFragment)
            showFragment(new SingUpFragment());
        }
    }

    private void showFragment(Fragment fragment) {
        // استبدال Fragment الحالي بالـ Fragment الجديد داخل الـ container
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.account_fragment, fragment); // استخدام الـ container المناسب
        transaction.addToBackStack(null); // إضافة الـ Fragment إلى back stack للرجوع إليه
        transaction.commit();
    }
}