package com.example.cleardayapplication.ui.fragments.task;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;

public class AddTaskFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "project_id";
    private static final String ARG_USER_ID = "user_id";

    private String projectId;
    private String userId;

    private EditText etTaskName, etTaskDescription;
    private Button btnSave, btnAddAttachment;
    private Button btnUrgent, btnRunning, btnOngoing;

    private String selectedStatus = "Running"; // الحالة الافتراضية
    private Uri attachmentUri;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

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

        if(getArguments() != null){
            projectId = getArguments().getString(ARG_PROJECT_ID);
            userId = getArguments().getString(ARG_USER_ID);
        }

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == getActivity().RESULT_OK && result.getData() != null){
                        attachmentUri = result.getData().getData();
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Attachment selected!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        etTaskName = view.findViewById(R.id.etTaskName);
        etTaskDescription = view.findViewById(R.id.etTaskDescription);
        btnSave = view.findViewById(R.id.btnSave);
        btnAddAttachment = view.findViewById(R.id.btnAddAttachment);

        // أزرار اختيار الحالة
        btnUrgent = view.findViewById(R.id.btnUrgent);
        btnRunning = view.findViewById(R.id.btnRunning);
        btnOngoing = view.findViewById(R.id.btnOngoing);

        btnAddAttachment.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        // Listener للحالات مع تغيير اللون
        View.OnClickListener statusClickListener = v -> {
            if(v == btnUrgent){
                selectedStatus = "Urgent";
                btnUrgent.setBackgroundColor(Color.RED);
                btnRunning.setBackgroundColor(Color.LTGRAY);
                btnOngoing.setBackgroundColor(Color.LTGRAY);
            } else if(v == btnRunning){
                selectedStatus = "Running";
                btnRunning.setBackgroundColor(Color.GREEN);
                btnUrgent.setBackgroundColor(Color.LTGRAY);
                btnOngoing.setBackgroundColor(Color.LTGRAY);
            } else if(v == btnOngoing){
                selectedStatus = "Ongoing";
                btnOngoing.setBackgroundColor(Color.parseColor("#800080")); // بنفسجي
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
        String taskName = etTaskName.getText().toString().trim();
        String taskDescription = etTaskDescription.getText().toString().trim();

        if(taskName.isEmpty()){ etTaskName.setError("Enter task name"); return; }
        if(taskDescription.isEmpty()){ etTaskDescription.setError("Enter description"); return; }

        Task task = new Task();
        task.setTitle(taskName);
        task.setDescription(taskDescription);
        task.setProjectId(projectId);
        task.setUserId(userId);
        task.setStatus(selectedStatus);
        task.setCreatedBy(userId);

        firestore.collection(Collections.TASKS)
                .add(task)
                .addOnSuccessListener(docRef -> {
                    String taskId = docRef.getId();
                    docRef.update("taskId", taskId);

                    // رفع المرفق إذا تم اختياره
                    if(attachmentUri != null) uploadAttachmentToTask(taskId, attachmentUri);

                    if(getContext() != null)
                        Toast.makeText(getContext(), "Task added successfully", Toast.LENGTH_SHORT).show();

                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Failed to add task: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadAttachmentToTask(String taskId, Uri fileUri){
        try{
            InputStream is = getContext().getContentResolver().openInputStream(fileUri);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();
            String base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);

            firestore.collection(Collections.TASKS)
                    .document(taskId)
                    .update("attachments", java.util.Collections.singletonList(base64))
                    .addOnSuccessListener(aVoid -> {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Attachment uploaded!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Failed to upload attachment: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch(Exception e){
            e.printStackTrace();
            if(getContext() != null)
                Toast.makeText(getContext(), "Error reading file", Toast.LENGTH_SHORT).show();
        }
    }
}