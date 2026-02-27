package com.example.cleardayapplication.ui.fragments.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentSingInBinding;
import com.example.cleardayapplication.ui.activitys.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SingInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentSingInBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // تهيئة Firebase Authentication
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ربط ViewBinding
        binding = FragmentSingInBinding.inflate(inflater, container, false);

        // إضافة حدث زر تسجيل الدخول
        binding.accountSingInButton.setOnClickListener(v -> {
            String email = binding.accountSingInEmail.getText().toString();
            String password = binding.accountSingInPassword.getText().toString();

            // تحقق من الحقول
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!email.contains("@")) {
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                // تسجيل الدخول
                signInUser(email, password);
            }
        });

        // إضافة حدث زر الانتقال إلى صفحة التسجيل
        binding.accountSingInSingUpButton.setOnClickListener(v -> {
            // الانتقال إلى شاشة التسجيل
            Intent intent = new Intent(getContext(), SingUpFragment.class); // تغيير هذه الوجهة إذا كانت شاشة أخرى
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // إذا تم تسجيل الدخول بنجاح
                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                        // الانتقال إلى الشاشة الرئيسية
                        Intent intent = new Intent(getContext(), HomeActivity.class); // تغيير هذه الوجهة إلى النشاط الرئيسي
                        startActivity(intent);
                        getActivity().finish(); // إنهاء النشاط الحالي لتجنب العودة له
                    } else {
                        // فشل في تسجيل الدخول
                        Toast.makeText(getContext(), "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}