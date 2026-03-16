package com.example.cleardayapplication.ui.fragments.project;

import static com.example.cleardayapplication.domain.utils.Collections.PROJECTS;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentAddProjectBinding;
import com.example.cleardayapplication.databinding.FragmentProjectsBinding;
import com.example.cleardayapplication.domain.model.Project;
import com.example.cleardayapplication.domain.utils.OnProjectAddedListener;
import com.example.cleardayapplication.ui.adapters.ProjectAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentAddProjectBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private OnProjectAddedListener listener;

    public void setOnProjectAddedListener(OnProjectAddedListener listener) {
        this.listener = listener;
    }

    public AddProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProjectFragment newInstance(String param1, String param2) {
        AddProjectFragment fragment = new AddProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddProjectBinding.inflate(inflater, container, false);
        // Init Firebase
        firestore   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // Cancel
        binding.btnCancel.setOnClickListener(v -> dismiss());

        // Add
        binding.btnAdd.setOnClickListener(v -> {
            if (validate()) {
                addProjectToFirestore();
            }
        });
        // Clear errors while typing
        binding.etTitle.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (s.length() > 0) binding.tvTitleError.setVisibility(View.GONE);
            }
        });

        binding.etDescription.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (s.length() > 0) binding.tvDescriptionError.setVisibility(View.GONE);
            }
        });
        return binding.getRoot();
    }

    private boolean validate() {
        String title = binding.etTitle.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        boolean isValid    = true;

        if (title.isEmpty()) {
            binding.tvTitleError.setVisibility(View.VISIBLE);
            binding.etTitle.requestFocus();
            isValid = false;
        }

        if (description.isEmpty()) {
            binding.tvDescriptionError.setVisibility(View.VISIBLE);
            if (isValid) binding.etDescription.requestFocus();
            isValid = false;
        }

        return isValid;
    }


    private void addProjectToFirestore() {
        String projectName  = binding.etTitle.getText().toString().trim();
        String description  = binding.etDescription.getText().toString().trim();
        String currentUserId = auth.getCurrentUser().getUid();

        // Build project object
        Project project = new Project();
        project.setProjectId(projectName);
        project.setName(projectName);
        project.setDescription(description);
        project.setAdminId(currentUserId);
        project.setStatus("open");
        project.setProjectMembersId(new ArrayList<>(Arrays.asList(currentUserId)));
        project.setTasksId(new ArrayList<>());
        project.setCreateAt(System.currentTimeMillis());

        // Disable button to prevent double tap
        binding.btnAdd.setEnabled(false);

        // Save to Firestore — document ID = project name
        firestore.collection(PROJECTS)
                .document(projectName)
                .set(project)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(),
                            "Project \"" + projectName + "\" added successfully!",
                            Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onProjectAdded();
                    dismiss(); // hide dialog
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Failed to add project: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    binding.btnAdd.setEnabled(true); // re-enable on failure
                });
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.90),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }


}