package com.example.cleardayapplication.ui.fragments.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private boolean isEditing = false;
    private Uri selectedImageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userId;

    // ✅ نقل الـ Launcher هنا ليعمل بشكل صحيح مع دورة حياة الفراغمنت
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (binding != null) {
                        Glide.with(this)
                                .load(selectedImageUri)
                                .circleCrop()
                                .placeholder(R.drawable.ic_account_circle)
                                .into(binding.profileImage);
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "ddwdujzxz");

        try {
            MediaManager.init(requireContext(), config);
        } catch (IllegalStateException e) {
            // already initialized
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            loadUserData();
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }

        binding.btnEdit.setOnClickListener(v -> {
            if (!isEditing) {
                enableEditing(true);
            } else {
                String newName = binding.etUserName.getText().toString().trim();
                String newEmail = binding.etEmail.getText().toString().trim();

                if (newName.isEmpty() || newEmail.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                showConfirmDialog(newName, newEmail);
            }
        });

        binding.profileImage.setOnClickListener(v -> {
            if (isEditing) openImagePicker();
        });

        return binding.getRoot();
    }

    private void enableEditing(boolean enable) {
        isEditing = enable;
        if (binding == null) return;

        if (enable) {
            binding.tvUserName.setVisibility(View.GONE);
            binding.tvEmail.setVisibility(View.GONE);
            // ✅ إظهار الحاوية بالكامل (TextInputLayout) وليس الحقل فقط
            binding.tilUserName.setVisibility(View.VISIBLE);
            binding.tilEmail.setVisibility(View.VISIBLE);
            binding.btnEdit.setText("Save");
        } else {
            binding.tvUserName.setVisibility(View.VISIBLE);
            binding.tvEmail.setVisibility(View.VISIBLE);
            // ✅ إخفاء الحاوية بالكامل
            binding.tilUserName.setVisibility(View.GONE);
            binding.tilEmail.setVisibility(View.GONE);
            binding.btnEdit.setText("Edit Profile");
        }
    }

    private void loadUserData() {
        firestore.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && binding != null) {
                        String name = documentSnapshot.getString("userName");
                        String email = documentSnapshot.getString("email");
                        String imageUrl = documentSnapshot.getString("profileImage");

                        binding.tvUserName.setText(name);
                        binding.tvEmail.setText(email);
                        binding.etUserName.setText(name);
                        binding.etEmail.setText(email);

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .circleCrop()
                                    .placeholder(R.drawable.ic_account_circle)
                                    .into(binding.profileImage);
                        }
                    }
                });
    }

    private void showConfirmDialog(String name, String email) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Update")
                .setMessage("Are you sure you want to update your profile?")
                .setPositiveButton("Yes", (dialog, which) -> updateUserData(name, email))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void updateUserData(String name, String email) {
        if (binding == null) return;
        binding.progressLoader.setVisibility(View.VISIBLE);

        if (selectedImageUri != null) {
            MediaManager.get().upload(selectedImageUri)
                    .unsigned("profile_preset")
                    .callback(new UploadCallback() {
                        @Override public void onStart(String requestId) {}
                        @Override public void onProgress(String requestId, long bytes, long totalBytes) {}

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String imageUrl = resultData.get("secure_url").toString();
                            saveDataToFirestore(name, email, imageUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            if (binding != null) {
                                binding.progressLoader.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override public void onReschedule(String requestId, ErrorInfo error) {}
                    })
                    .dispatch();
        } else {
            saveDataToFirestore(name, email, null);
        }
    }

    private void saveDataToFirestore(String name, String email, @Nullable String imageUrl) {
        Map<String, Object> update = new HashMap<>();
        update.put("userName", name);
        update.put("email", email);
        if (imageUrl != null) update.put("profileImage", imageUrl);

        firestore.collection("User")
                .document(userId)
                .set(update, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    if (binding == null) return;

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) user.updateEmail(email);

                    binding.tvUserName.setText(name);
                    binding.tvEmail.setText(email);
                    enableEditing(false);
                    binding.progressLoader.setVisibility(View.GONE);

                    if (imageUrl != null) {
                        Glide.with(this)
                                .load(imageUrl)
                                .circleCrop()
                                .placeholder(R.drawable.ic_account_circle)
                                .into(binding.profileImage);
                    }

                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    if (binding != null) {
                        binding.progressLoader.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
