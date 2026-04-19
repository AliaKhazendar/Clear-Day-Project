package com.example.cleardayapplication.ui.fragments.task;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentAddProjectBinding;
import com.example.cleardayapplication.databinding.FragmentEditTasksInformationBinding;
import com.example.cleardayapplication.domain.model.Project;
import com.example.cleardayapplication.domain.model.Task;
import com.example.cleardayapplication.domain.utils.Collections;
import com.example.cleardayapplication.domain.utils.OnProjectAddedListener;
import com.example.cleardayapplication.domain.utils.OnTaskEditedDoneListener;
import com.example.cleardayapplication.domain.utils.OnTaskEditedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTasksInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTasksInformationFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "taskId";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String idParm;
    private String mParam2;

    private FragmentEditTasksInformationBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

        private OnTaskEditedDoneListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       // listener = (OnTaskEditedListener) context;
    }
    public void setOnTakEditedListener(OnTaskEditedDoneListener listener) {
        this.listener = listener;
    }

    public EditTasksInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id task id.
     * @return A new instance of fragment EditTasksInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTasksInformationFragment newInstance(String id) {
        EditTasksInformationFragment fragment = new EditTasksInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idParm = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (idParm == null) dismiss();
        else {
            binding = FragmentEditTasksInformationBinding.inflate(inflater, container, false);
            // Init Firebase
            firestore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            getTaskInfo();
            // Cancel
            binding.btnCancel.setOnClickListener(v -> dismiss());

            // Add
            binding.btnEdit.setOnClickListener(v -> {
                if (validate()) {
                    editTaskInFirestore();
                }
            });
            // Clear errors while typing
            binding.etTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) binding.tvTitleError.setVisibility(View.GONE);
                }
            });

            binding.etDescription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) binding.tvDescriptionError.setVisibility(View.GONE);
                }
            });
        }
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

    private void editTaskInFirestore(){
        String taskTitle  = binding.etTitle.getText().toString().trim();
        String description  = binding.etDescription.getText().toString().trim();
        Map<String, Object> editMap = new HashMap<>();
        editMap.put(TITLE, taskTitle);
        editMap.put(DESCRIPTION, description);
        firestore.collection(Collections.TASKS)
                .document(idParm)
                .update(editMap)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(requireContext(), "UpdatedSuccessfully", Toast.LENGTH_SHORT).show();
                    listener.onTaskEditedDone();
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                    Log.e("Firestore", "Error updating task", e);
                });
    }

    private void getTaskInfo(){
        String currentUserId = auth.getCurrentUser().getUid();
        firestore.collection(Collections.TASKS)
                .document(idParm)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Task task = documentSnapshot.toObject(Task.class);
                        if (task != null && task.getCreatedBy().equals(currentUserId) && task.getTaskId().equals(idParm)) {
                            binding.etTitle.setText(task.getTitle());
                            binding.etDescription.setText(task.getDescription());
                        }
                        else{
                            // Manage the views visibility to prevent invalid update
                            binding.etTitle.setVisibility(View.GONE);
                            binding.tvTitleError.setVisibility(View.GONE);
                            binding.tvDescriptionError.setVisibility(View.GONE);
                            binding.etDescription.setVisibility(View.GONE);
                            binding.tvDialogTitle.setVisibility(View.GONE);
                            binding.btnEdit.setVisibility(View.GONE);
                            binding.tvBeeSign.setVisibility(View.VISIBLE);
                            binding.tvInfoMessage.setVisibility(View.VISIBLE);
                        }
                    }
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