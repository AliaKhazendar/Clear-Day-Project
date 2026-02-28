package com.example.cleardayapplication.ui.fragments.account;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentSingUpBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SingUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentSingUpBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // تهيئة Firebase Authentication
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // ربط ViewBinding
        binding = FragmentSingUpBinding.inflate(inflater, container, false);

        // إضافة حدث زر إنشاء الحساب
        binding.accountSingUpButton.setOnClickListener(v -> {
            binding.progressLoaderSingUp.setVisibility(VISIBLE);
            String userName = binding.accountSingUpUserName.getText().toString();
            String email = binding.accountSingUpEmail.getText().toString();
            String password = binding.accountSingUpPassword.getText().toString();

            binding.accountSingUpButton.setCheckable(false);
            // تحقق من الحقول
            if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!email.contains("@")) {
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                // إنشاء الحساب
                createAccount(email, password);
            }
            binding.progressLoaderSingUp.setVisibility(INVISIBLE);
            binding.accountSingUpButton.setCheckable(true);
        });

        // إضافة حدث زر تسجيل الدخول
        binding.accountSingUpSingInButton.setOnClickListener(v -> {
            // هنا يمكنك استخدام Intent للتنقل إلى شاشة تسجيل الدخول
            Fragment fragment = new SingInFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.account_fragment, fragment)
                    .commit();
        });

        return binding.getRoot();
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // إرسال رسالة تأكيد للبريد الإلكتروني بعد إنشاء الحساب
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // إنشاء الحساب ناجح، تم إرسال رسالة تأكيد البريد الإلكتروني
                                        Toast.makeText(getContext(), "Account Created Successfully. Please verify your email.", Toast.LENGTH_LONG).show();

                                        // الانتقال إلى شاشة تسجيل الدخول باستخدام Intent
                                        Intent intent = new Intent(getContext(), SingInFragment.class); // تغيير هذه الوجهة إذا كانت شاشة أخرى
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // فشل في إنشاء الحساب
                        Toast.makeText(getContext(), "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }

                });
    }
}