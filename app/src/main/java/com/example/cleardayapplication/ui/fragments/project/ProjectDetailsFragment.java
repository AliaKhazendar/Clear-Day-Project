package com.example.cleardayapplication.ui.fragments.project;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentProjectDetailesBinding;
import com.example.cleardayapplication.domain.model.Project;
import com.example.cleardayapplication.domain.model.Task;
import com.example.cleardayapplication.domain.utils.Collections;
import com.example.cleardayapplication.domain.utils.OnGoToInvitePeopleListener;
import com.example.cleardayapplication.domain.utils.OnItemClicks;
import com.example.cleardayapplication.domain.utils.OnTaskEditedListener;
import com.example.cleardayapplication.ui.adapters.TaskAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailsFragment extends Fragment implements OnItemClicks {

    private static final String TAG        = "PROJECT_DETAILS";
    private static final String PROJECT_ID = "project_id";

    private String    projectId;
    private TaskAdapter adapter;
    FirebaseAuth      auth;
    FirebaseFirestore firestore;

    private OnAddTaskListener          addTaskListener;
    private OnGoToInvitePeopleListener goToInvitePeopleListener;
    private OnTaskEditedListener       editTaskListener;

    public interface OnAddTaskListener {
        void onAddTaskClicked(String projectId, String userId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        goToInvitePeopleListener = (OnGoToInvitePeopleListener) context;
        editTaskListener         = (OnTaskEditedListener) context;
    }

    public void setOnAddTaskListener(OnAddTaskListener listener) {
        this.addTaskListener = listener;
    }

    private List<Task> tasksList = new ArrayList<>();
    private FragmentProjectDetailesBinding binding;

    public ProjectDetailsFragment() {}

    public static ProjectDetailsFragment newInstance(Object obj) {
        ProjectDetailsFragment fragment = new ProjectDetailsFragment();
        Bundle args = new Bundle();
        if (obj instanceof Project) {
            Project p = (Project) obj;
            args.putString(PROJECT_ID, p.getProjectId());
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectId = getArguments().getString(PROJECT_ID);
        }
        Log.d(TAG, "onCreate — projectId: " + projectId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding   = FragmentProjectDetailesBinding.inflate(inflater, container, false);
        auth      = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        setupRecyclerView();
        loadProjectAndTasks();
        setupMenu();

        return binding.getRoot();
    }

    // load project info, then get tasksId array
    // ─── Simpler: query Task collection directly by projectId ─────────────────
    public void loadProjectAndTasks() {
        if (projectId == null || projectId.isEmpty()) {
            Log.e(TAG, "projectId is NULL — aborting load");
            return;
        }

        binding.progressLoaderPD.setVisibility(View.VISIBLE);
        binding.emptyStatePD.setVisibility(View.GONE);

        // Step 1: load project info (name, description)
        firestore.collection(Collections.PROJECTS)
                .document(projectId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Project project = snapshot.toObject(Project.class);
                        if (project != null) {
                            binding.projectName.setText(project.getName());
                            binding.projectDescription.setText(project.getDescription());
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Failed to load project info: " + e.getMessage()));

        // Step 2: query Task collection directly by projectId field
        firestore.collection(Collections.TASKS)
                .whereEqualTo("projectId", projectId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);

                    Log.d(TAG, "Query returned: " + querySnapshot.size() + " tasks");

                    // Log each document for debugging
                    for (var doc : querySnapshot.getDocuments()) {
                        Log.d(TAG, "Doc ID: " + doc.getId() + " | data: " + doc.getData());
                    }

                    if (!querySnapshot.isEmpty()) {
                        tasksList = querySnapshot.toObjects(Task.class);
                        Log.d(TAG, "tasksList size after parse: " + tasksList.size());
                        for (Task t : tasksList) {
                            Log.d(TAG, "  → title='" + t.getTitle() + "' status='" + t.getStatus() + "' id='" + t.getTaskId() + "'");
                        }
                        adapter.updateList(tasksList);
                        binding.emptyStatePD.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, "No tasks found for projectId: " + projectId);
                        binding.emptyStatePD.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Log.e(TAG, "Failed to load tasks: " + e.getMessage());
                    Toast.makeText(getContext(),
                            "Failed to load tasks: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    // query Task collection using the IDs
    private void loadTasksByIds(List<String> taskIds) {
        // Firestore whereIn supports max 10 — handle pagination if needed
        // For now assuming <= 10 tasks
        firestore.collection(Collections.TASKS)
                .whereIn("taskId", taskIds)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);

                    if (!querySnapshot.isEmpty()) {
                        tasksList = querySnapshot.toObjects(Task.class);
                        Log.d(TAG, "Tasks loaded: " + tasksList.size());
                        adapter.updateList(tasksList);
                        binding.emptyStatePD.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, "No matching tasks found in Task collection");
                        binding.emptyStatePD.setVisibility(VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Log.e(TAG, "Failed to load tasks: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to load tasks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    void setupRecyclerView() {
        tasksList = new ArrayList<>();
        adapter = new TaskAdapter(tasksList, this, editTaskListener);
        binding.tasksRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.tasksRecycler.setAdapter(adapter);
        Log.d(TAG, "RecyclerView setup done");
    }

    private void setupMenu() {
        binding.actionsMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), binding.actionsMenu);
            popup.getMenuInflater().inflate(R.menu.task_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.menu_update_project) {
                    firestore.collection(Collections.PROJECTS)
                            .document(projectId)
                            .get()
                            .addOnSuccessListener(snapshot -> {
                                Project project = snapshot.toObject(Project.class);
                                if (project != null) showEditProjectDialog(project);
                            });
                    return true;

                } else if (id == R.id.menu_delete_project) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Delete Project")
                            .setMessage("Are you sure you want to delete this project and all its tasks?")
                            .setPositiveButton("Delete", (dialog, which) -> deleteProjectAndTasks())
                            .setNegativeButton("Cancel", null)
                            .show();
                    return true;

                } else if (id == R.id.menu_invite_new_user) {
                    goToInvitePeopleListener.onGoToInvitePeople(projectId);
                    return true;

                } else if (id == R.id.menu_delete_all_tasks) {
                    // implement if needed
                    return true;
                }

                return false;
            });

            popup.show();
        });
    }

    @Override
    public void onCardItemClick(Object obj) {
        // navigate to task details screen
    }

    public void showEditProjectDialog(Project project) {
        View view = getLayoutInflater().inflate(R.layout.dialog_update_project, null);

        EditText inputTitle = view.findViewById(R.id.project_title_update);
        EditText inputDesc  = view.findViewById(R.id.project_description_update);

        inputTitle.setText(project.getName());
        inputDesc.setText(project.getDescription());

        new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Update Project")
                .setPositiveButton("Save", (dialog, id) ->
                        updateProjectInDatabase(
                                inputTitle.getText().toString().trim(),
                                inputDesc.getText().toString().trim()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void updateProjectInDatabase(String projectName, String projectDescription) {
        if (projectId == null) return;

        binding.progressLoaderPD.setVisibility(View.VISIBLE);

        firestore.collection(Collections.PROJECTS)
                .document(projectId)
                .update(Collections.PROJECT_NAME, projectName,
                        Collections.PROJECT_DESCRIPTION, projectDescription)
                .addOnSuccessListener(aVoid -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Project updated successfully", Toast.LENGTH_SHORT).show();
                    loadProjectAndTasks(); // reload after update
                })
                .addOnFailureListener(e -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteProjectAndTasks() {
        if (projectId == null) return;

        binding.progressLoaderPD.setVisibility(VISIBLE);

        // Collect task IDs currently shown
        List<String> taskIdsToDelete = new ArrayList<>();
        for (Task t : tasksList) {
            if (t.getTaskId() != null && !t.getTaskId().isEmpty()) {
                taskIdsToDelete.add(t.getTaskId());
            }
        }

        // Delete each task document
        for (String taskId : taskIdsToDelete) {
            firestore.collection(Collections.TASKS).document(taskId).delete()
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to delete task " + taskId + ": " + e.getMessage()));
        }

        // Delete the project document itself
        firestore.collection(Collections.PROJECTS)
                .document(projectId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Project deleted successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new ProjectsFragment())
                            .commit();
                })
                .addOnFailureListener(e -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed to delete project: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public String getProjectId() { return projectId; }
    public FirebaseAuth getAuth() { return auth; }
}