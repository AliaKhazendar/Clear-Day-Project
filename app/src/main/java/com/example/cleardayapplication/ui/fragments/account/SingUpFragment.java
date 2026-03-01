package com.example.cleardayapplication.ui.fragments.account;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSingUpBinding.inflate(inflater, container, false);

        // زر إنشاء الحساب
        binding.accountSingUpButton.setOnClickListener(v -> {

            String userName = binding.accountSingUpUserName.getText().toString().trim();
            String email = binding.accountSingUpEmail.getText().toString().trim();
            String password = binding.accountSingUpPassword.getText().toString().trim();

            if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(),
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@")) {
                Toast.makeText(getContext(),
                        "Please enter a valid email",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getContext(),
                        "Password should be at least 6 characters",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // إظهار اللودر وتعطيل الزر
            binding.progressLoaderSingUp.setVisibility(VISIBLE);
            binding.accountSingUpButton.setEnabled(false);

            createAccount(email, password);
        });

        // زر الانتقال لتسجيل الدخول
        binding.accountSingUpSingInButton.setOnClickListener(v -> {
            openSignInFragment();
        });

        return binding.getRoot();
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {

                        mAuth.getCurrentUser()
                                .sendEmailVerification()
                                .addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful()) {

                                        Toast.makeText(getContext(),
                                                "Account Created Successfully. Please verify your email.",
                                                Toast.LENGTH_LONG).show();

                                        openSignInFragment();

                                    } else {
                                        Toast.makeText(getContext(),
                                                "Failed to send verification email.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    resetUI();
                                });

                    } else {

                        Toast.makeText(getContext(),
                                "Authentication Failed: "
                                        + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                        resetUI();
                    }
                });
    }

    private void openSignInFragment() {

        Fragment fragment = new SingInFragment();

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.account_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void resetUI() {
        binding.progressLoaderSingUp.setVisibility(INVISIBLE);
        binding.accountSingUpButton.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}