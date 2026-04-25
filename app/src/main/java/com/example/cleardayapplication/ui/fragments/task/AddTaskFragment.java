package com.example.cleardayapplication.ui.fragments.task;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.domain.model.Task;
import com.example.cleardayapplication.domain.utils.Collections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;

public class AddTaskFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "project_id";
    private static final String ARG_USER_ID    = "user_id";
    private static final String TAG            = "ADD_TASK";

    private String projectId;
    private String userId;

    private EditText etTaskName, etTaskDescription;
    private Button   btnSave, btnAddAttachment;
    private Button   btnUrgent, btnRunning, btnOngoing;

    private String selectedStatus = "Running";
    private Uri    attachmentUri;

    private FirebaseFirestore firestore;
    private FirebaseAuth      auth;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    public AddTaskFragment() {}

    public static AddTaskFragment newInstance(String projectId, String userId) {
        AddTaskFragment fragment = new AddTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectId);
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            projectId = getArguments().getString(ARG_PROJECT_ID);
            userId    = getArguments().getString(ARG_USER_ID);
        }

        Log.d(TAG, "projectId=" + projectId + " | userId=" + userId);

        firestore = FirebaseFirestore.getInstance();
        auth      = FirebaseAuth.getInstance();

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK
                            && result.getData() != null) {
                        attachmentUri = result.getData().getData();
                        if (getContext() != null)
                            Toast.makeText(getContext(), "Attachment selected!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        etTaskName        = view.findViewById(R.id.etTaskName);
        etTaskDescription = view.findViewById(R.id.etTaskDescription);
        btnSave           = view.findViewById(R.id.btnSave);
        btnAddAttachment  = view.findViewById(R.id.btnAddAttachment);
        btnUrgent         = view.findViewById(R.id.btnUrgent);
        btnRunning        = view.findViewById(R.id.btnRunning);
        btnOngoing        = view.findViewById(R.id.btnOngoing);

        btnAddAttachment.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        View.OnClickListener statusClickListener = v -> {
            if (v == btnUrgent) {
                selectedStatus = "Urgent";
                btnUrgent.setBackgroundColor(Color.RED);
                btnRunning.setBackgroundColor(Color.LTGRAY);
                btnOngoing.setBackgroundColor(Color.LTGRAY);
            } else if (v == btnRunning) {
                selectedStatus = "Running";
                btnRunning.setBackgroundColor(Color.GREEN);
                btnUrgent.setBackgroundColor(Color.LTGRAY);
                btnOngoing.setBackgroundColor(Color.LTGRAY);
            } else if (v == btnOngoing) {
                selectedStatus = "Ongoing";
                btnOngoing.setBackgroundColor(Color.parseColor("#800080"));
                btnUrgent.setBackgroundColor(Color.LTGRAY);
                btnRunning.setBackgroundColor(Color.LTGRAY);
            }
        };

        btnUrgent.setOnClickListener(statusClickListener);
        btnRunning.setOnClickListener(statusClickListener);
        btnOngoing.setOnClickListener(statusClickListener);
        btnSave.setOnClickListener(v -> saveTask());

        return view;
    }

    private void saveTask() {
        String taskName        = etTaskName.getText().toString().trim();
        String taskDescription = etTaskDescription.getText().toString().trim();

        if (taskName.isEmpty()) {
            etTaskName.setError("Enter task name");
            return;
        }
        if (taskDescription.isEmpty()) {
            etTaskDescription.setError("Enter description");
            return;
        }
        if (projectId == null || projectId.isEmpty()) {
            Toast.makeText(getContext(), "Error: missing project ID!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "saveTask: projectId is NULL or empty");
            return;
        }

        btnSave.setEnabled(false); // prevent double tap

        // Step 1: generate document ID upfront
        String taskId = firestore.collection(Collections.TASKS).document().getId();

        // Step 2: build Task object
        Task task = new Task();
        task.setTaskId(taskId);
        task.setTitle(taskName);
        task.setDescription(taskDescription);
        task.setProjectId(projectId);
        task.setUserId(userId);
        task.setStatus(selectedStatus);
        task.setCreatedBy(userId);

        Log.d(TAG, "Saving task: " + taskId + " under project: " + projectId);

        // Step 3: save Task document in Task collection
        firestore.collection(Collections.TASKS)
                .document(taskId)
                .set(task)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Task saved in Task collection");

                    // Step 4: add taskId to the Project's tasksId array
                    firestore.collection(Collections.PROJECTS)
                            .document(projectId)
                            .update("tasksId", FieldValue.arrayUnion(taskId))
                            .addOnSuccessListener(aVoid2 -> {
                                Log.d(TAG, "tasksId updated in Project document");

                                if (attachmentUri != null) {
                                    uploadAttachmentToTask(taskId, attachmentUri);
                                } else {
                                    if (getContext() != null)
                                        Toast.makeText(getContext(), "Task added successfully!", Toast.LENGTH_SHORT).show();
                                    getParentFragmentManager().popBackStack();
                                }
                            })
                            .addOnFailureListener(e -> {
                                btnSave.setEnabled(true);
                                Log.e(TAG, "Failed to update project tasksId: " + e.getMessage());
                                if (getContext() != null)
                                    Toast.makeText(getContext(),
                                            "Task saved but project link failed: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack();
                            });
                })
                .addOnFailureListener(e -> {
                    btnSave.setEnabled(true);
                    Log.e(TAG, "Failed to save task: " + e.getMessage());
                    if (getContext() != null)
                        Toast.makeText(getContext(), "Failed to add task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadAttachmentToTask(String taskId, Uri fileUri) {
        try {
            InputStream is    = getContext().getContentResolver().openInputStream(fileUri);
            byte[]      bytes = new byte[is.available()];
            is.read(bytes);
            is.close();

            String base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            firestore.collection(Collections.TASKS)
                    .document(taskId)
                    .update("attachments", java.util.Collections.singletonList(base64))
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Attachment uploaded");
                        if (getContext() != null)
                            Toast.makeText(getContext(), "Task added with attachment!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack(); // navigate back AFTER upload finishes
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Attachment upload failed: " + e.getMessage());
                        if (getContext() != null)
                            Toast.makeText(getContext(),
                                    "Task saved but attachment failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error reading file: " + e.getMessage());
            if (getContext() != null)
                Toast.makeText(getContext(), "Error reading file", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }
    }
}