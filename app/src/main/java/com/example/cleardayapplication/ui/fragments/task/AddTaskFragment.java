package com.example.cleardayapplication.ui.fragments.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.domain.model.Task;
import com.example.cleardayapplication.domain.utils.Collections;
import com.example.cleardayapplication.ui.activitys.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "project_id";
    private static final String ARG_USER_ID = "user_id";

    private String projectId;
    private String userId;

    private EditText etTaskName, etDate, etStart, etEnd, etTaskDescription;
    private Button btnUrgent, btnRunning, btnOngoing, btnSave;

    private String selectedStatus = "Running"; // Default

    private FirebaseFirestore firestore;

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
            userId = getArguments().getString(ARG_USER_ID);
        }
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        etTaskName = view.findViewById(R.id.etTaskName);
        etTaskDescription = view.findViewById(R.id.etTaskDescription);
        etDate = view.findViewById(R.id.etDate);
        etStart = view.findViewById(R.id.etStart);
        etEnd = view.findViewById(R.id.etEnd);

        btnUrgent = view.findViewById(R.id.btnUrgent);
        btnRunning = view.findViewById(R.id.btnRunning);
        btnOngoing = view.findViewById(R.id.btnOngoing);
        btnSave = view.findViewById(R.id.btnSave);

        // Board Selection
        btnUrgent.setOnClickListener(v -> selectBoard("Urgent"));
        btnRunning.setOnClickListener(v -> selectBoard("Running"));
        btnOngoing.setOnClickListener(v -> selectBoard("Ongoing"));

        // DatePicker
        etDate.setOnClickListener(v -> showDatePicker());

        // TimePickers
        etStart.setOnClickListener(v -> showTimePicker(etStart));
        etEnd.setOnClickListener(v -> showTimePicker(etEnd));

        btnSave.setOnClickListener(v -> saveTask());

        ImageView btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }

        // Initialize status selection colors
        selectBoard(selectedStatus);

        return view;
    }

    private void selectBoard(String status) {
        selectedStatus = status;

        int activeColor = ContextCompat.getColor(requireContext(), R.color.purple); // Default active color
        int inactiveColor = ContextCompat.getColor(requireContext(), R.color.light_gray);

        if ("Urgent".equals(status)) activeColor = ContextCompat.getColor(requireContext(), R.color.red);
        else if ("Running".equals(status)) activeColor = ContextCompat.getColor(requireContext(), R.color.green);

        btnUrgent.setBackgroundTintList(ColorStateList.valueOf(status.equals("Urgent") ? activeColor : inactiveColor));
        btnRunning.setBackgroundTintList(ColorStateList.valueOf(status.equals("Running") ? activeColor : inactiveColor));
        btnOngoing.setBackgroundTintList(ColorStateList.valueOf(status.equals("Ongoing") ? activeColor : inactiveColor));
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(requireContext(),
                (view, y, m, d) -> etDate.setText(String.format("%02d/%02d/%04d", d, m + 1, y)),
                year, month, day);
        dpd.show();
    }

    private void showTimePicker(EditText et) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(requireContext(),
                (view, h, m) -> {
                    String amPm = (h >= 12) ? "PM" : "AM";
                    int hour12 = h % 12;
                    if (hour12 == 0) hour12 = 12;
                    et.setText(String.format("%d:%02d %s", hour12, m, amPm));
                }, hour, minute, false);
        tpd.show();
    }

    private void saveTask() {
        String taskName = etTaskName.getText().toString().trim();
        String taskDescription = etTaskDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String startTime = etStart.getText().toString().trim();
        String endTime = etEnd.getText().toString().trim();

        // Validation
        if (taskName.isEmpty()) { etTaskName.setError("Enter task name"); return; }
        if (taskDescription.isEmpty()) { etTaskDescription.setError("Enter description"); return; }
        if (date.isEmpty()) { etDate.setError("Select date"); return; }
        if (startTime.isEmpty()) { etStart.setError("Select start time"); return; }
        if (endTime.isEmpty()) { etEnd.setError("Select end time"); return; }

        Task task = new Task();
        task.setTitle(taskName);
        task.setDescription(taskDescription);
        task.setDate(date);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setProjectId(projectId);
        task.setUserId(userId);
        task.setStatus(selectedStatus);
        task.setCreatedBy(userId);

        firestore.collection(Collections.TASKS)
                .add(task)
                .addOnSuccessListener(docRef -> {
                    // Update document with its own ID for easier reference
                    String taskId = docRef.getId();
                    docRef.update("taskId", taskId);
                    
                    Toast.makeText(getContext(), "Task added successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); 
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(getActivity() instanceof HomeActivity){
            ((HomeActivity)getActivity()).binding.fabAdd.setVisibility(View.VISIBLE);
        }
    }
}
