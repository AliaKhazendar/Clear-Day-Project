package com.example.cleardayapplication.ui.fragments.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.domain.model.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "project_id";
    private static final String ARG_USER_ID = "user_id";

    private String projectId;
    private String userId;

    private EditText etTaskName, etDate, etStart, etEnd;
    private Button btnUrgent, btnRunning, btnOngoing, btnSave;

    private String selectedStatus = "Running"; // افتراضي

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
        etDate = view.findViewById(R.id.etDate);
        etStart = view.findViewById(R.id.etStart);
        etEnd = view.findViewById(R.id.etEnd);

        btnUrgent = view.findViewById(R.id.btnUrgent);
        btnRunning = view.findViewById(R.id.btnRunning);
        btnOngoing = view.findViewById(R.id.btnOngoing);
        btnSave = view.findViewById(R.id.btnSave);

        // اختيار Board
        btnUrgent.setOnClickListener(v -> selectBoard("Urgent"));
        btnRunning.setOnClickListener(v -> selectBoard("Running"));
        btnOngoing.setOnClickListener(v -> selectBoard("Ongoing"));

        // DatePicker
        etDate.setOnClickListener(v -> showDatePicker());

        // TimePickers
        etStart.setOnClickListener(v -> showTimePicker(etStart));
        etEnd.setOnClickListener(v -> showTimePicker(etEnd));

        btnSave.setOnClickListener(v -> saveTask());

        return view;
    }

    private void selectBoard(String status) {
        selectedStatus = status;

        // تحديث ألوان الأزرار
        btnUrgent.setBackgroundTintList(getResources().getColorStateList(
                status.equals("Urgent") ? R.color.purple : R.color.light_gray));
        btnRunning.setBackgroundTintList(getResources().getColorStateList(
                status.equals("Running") ? R.color.purple : R.color.light_gray));
        btnOngoing.setBackgroundTintList(getResources().getColorStateList(
                status.equals("Ongoing") ? R.color.purple : R.color.light_gray));
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
        String date = etDate.getText().toString().trim();
        String startTime = etStart.getText().toString().trim();
        String endTime = etEnd.getText().toString().trim();

        // Validation
        if (taskName.isEmpty()) { etTaskName.setError("Enter task name"); return; }
        if (date.isEmpty()) { etDate.setError("Select date"); return; }
        if (startTime.isEmpty()) { etStart.setError("Select start time"); return; }
        if (endTime.isEmpty()) { etEnd.setError("Select end time"); return; }

        Task task = new Task();
        task.setTitle(taskName);
        task.setDate(date);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setProjectId(projectId);
        task.setUserId(userId);
        task.setStatus(selectedStatus);

        firestore.collection("tasks")
                .add(task)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(getContext(), "Task added successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // ارجع للشاشة السابقة
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(),
                        "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}