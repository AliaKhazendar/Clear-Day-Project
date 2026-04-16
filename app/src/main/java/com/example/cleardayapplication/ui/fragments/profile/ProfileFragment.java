package com.example.cleardayapplication.ui.fragments.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.rpc.ErrorInfo;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;
    private boolean isEditing = false;
    private Uri selectedImageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userId;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentProfileBinding binding = FragmentProfileBinding.inflate(inflater, container, false);



        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // ✅ إعداد Cloudinary (بدون api_secret)
        Map<String,String> config = new HashMap();
        config.put("cloud_name", "ddwdujzxz"); // 👈 غيريها إذا لزم

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

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        Glide.with(this)
                                .load(selectedImageUri)
                                .circleCrop()
                                .placeholder(R.drawable.ic_account_circle)
                                .into(binding.profileImage);
                    }
                });

        return binding.getRoot();
    }
    private void enableEditing(boolean enable) {
        isEditing = enable;
        if (enable) {
            binding.tvUserName.setVisibility(View.GONE);
            binding.tvEmail.setVisibility(View.GONE);
            binding.etUserName.setVisibility(View.VISIBLE);
            binding.etEmail.setVisibility(View.VISIBLE);
            binding.btnEdit.setText("Save");
        } else {
            binding.tvUserName.setVisibility(View.VISIBLE);
            binding.tvEmail.setVisibility(View.VISIBLE);
            binding.etUserName.setVisibility(View.GONE);
            binding.etEmail.setVisibility(View.GONE);
            binding.btnEdit.setText("Edit Profile");
        }
    }

    private void loadUserData() {
        firestore.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
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
        binding.progressLoader.setVisibility(View.VISIBLE);

        if (selectedImageUri != null) {

            MediaManager.get().upload(selectedImageUri)
                    .unsigned("profile_preset") // 👈 مهم جداً (نفس الاسم في Cloudinary)
                    .callback(new UploadCallback() {

                        @Override
                        public void onStart(String requestId) {}

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {}

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            String imageUrl = resultData.get("secure_url").toString();
                            saveDataToFirestore(name, email, imageUrl);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            binding.progressLoader.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {}
                    })
                    .dispatch();

        } else {
            saveDataToFirestore(name, email, null);
        }
    }

    private void saveDataToFirestore(String name, String email, String imageUrl) {
        Map<String, Object> update = new HashMap<>();
        update.put("userName", name);
        update.put("email", email);
        if (imageUrl != null) update.put("profileImage", imageUrl);

        firestore.collection("User")
                .document(userId)
                .set(update, SetOptions.merge())
                .addOnSuccessListener(unused -> {
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
                    binding.progressLoader.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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