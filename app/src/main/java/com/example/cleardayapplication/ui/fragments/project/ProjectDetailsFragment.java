package com.example.cleardayapplication.ui.fragments.project;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
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
import com.example.cleardayapplication.ui.fragments.task.AddTaskFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailsFragment extends Fragment implements OnItemClicks{

    private static final String PROJECT_ID = "project_id";

    private String projectId;
    private TaskAdapter adapter;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private OnAddTaskListener addTaskListener;
    private OnGoToInvitePeopleListener goToInvitePeopleListener;
    private OnTaskEditedListener editTaskListener;
    public interface OnAddTaskListener {
        void onAddTaskClicked(String projectId, String userId);
    }


    // Only ONE onAttach — assign both
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        goToInvitePeopleListener = (OnGoToInvitePeopleListener) context;
        editTaskListener = (OnTaskEditedListener) context;
    }

    public void setOnAddTaskListener(OnAddTaskListener listener){
        this.addTaskListener = listener;

    }


    private List<Task> tasksList = new ArrayList<>();


    private FragmentProjectDetailesBinding binding;

    public ProjectDetailsFragment() {
        // Required empty public constructor
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProjectDetailesBinding
                .inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        getProjectDetails();
        setupRecyclerView();
        getTaskList();

        setupMenu();

        return binding.getRoot();
    }

    public void getProjectDetails() {
        if (projectId == null) return;

        binding.progressLoaderPD.setVisibility(View.VISIBLE);
        firestore.collection(Collections.PROJECTS)
                .document(projectId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    if (documentSnapshot.exists()) {
                        Project project = documentSnapshot.toObject(Project.class);
                        if (project != null) {
                            // تحديث واجهة المستخدم بالبيانات المجلوبة
                            binding.projectName.setText(project.getName());
                            binding.projectDescription.setText(project.getDescription());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error loading project details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    void setupRecyclerView(){

        adapter = new TaskAdapter(tasksList, this, editTaskListener);

        binding.tasksRecycler.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.tasksRecycler.setAdapter(adapter);

    }

    public void getTaskList(){
       // get task list from fire store
        binding.progressLoaderPD.setVisibility(VISIBLE);
        firestore.collection(Collections.TASKS)
                .whereEqualTo(Collections.PROJECT_ID,projectId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("TAG", "getTaskList: 00");
                    binding.progressLoaderPD.setVisibility(View.GONE);

                    if(!queryDocumentSnapshots.isEmpty()){
                        tasksList = queryDocumentSnapshots.toObjects(Task.class);
                        Log.d("TAG", "getTaskList: "+ tasksList.size());
                        adapter.updateList(tasksList);
                        binding.emptyStatePD.setVisibility(View.GONE);
                    }else{
                        binding.emptyStatePD.setVisibility(VISIBLE);
                    }

                }).addOnFailureListener(exception->{
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(),
                            "Failed to load projects: " + exception.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });

    }
    private void setupMenu() {

        binding.actionsMenu.setOnClickListener(v -> {

            PopupMenu popup = new PopupMenu(requireContext(), binding.actionsMenu);

            popup.getMenuInflater().inflate(R.menu.task_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();

                if (id == R.id.menu_update_project) {

                    firestore.collection(Collections.PROJECTS).document(projectId).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                Project project = documentSnapshot.toObject(Project.class);
                                if (project != null) {
                                    showEditProjectDialog(project);
                                }
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
                }
//                else if (id == R.id.menu_add_task) {
//                    if (projectId != null && auth.getCurrentUser() != null) {
//                        String userId = auth.getCurrentUser().getUid();
//
//                        // افتح Fragment إضافة مهمة مع تمرير الـ projectId و userId
//                        AddTaskFragment fragment = AddTaskFragment.newInstance(projectId, userId);
//                        getParentFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.fragment_container, fragment)
//                                .addToBackStack(null)
//                                .commit();
//
//                    }
//                    return true;
//                }
                else if (id == R.id.menu_delete_all_tasks) {
                    return true;
                }

                return false;
            });

            popup.show();
        });
    }
    // go to task details
    @Override
    public void onCardItemClick(Object obj) {

    }
    // update function
    public void showEditProjectDialog(Project project) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_update_project, null);

        EditText inputDesc = view.findViewById(R.id.project_description_update);
        EditText inputTitle = view.findViewById(R.id.project_title_update);

        inputTitle.setText(project.getName());
        inputDesc.setText(project.getDescription());

        builder.setView(view)
                .setTitle("Update Project")
                .setPositiveButton("Save", (dialog, id) -> {
                    String newTitle = inputTitle.getText().toString();
                    String newDesc = inputDesc.getText().toString();
                    updateProjectInDatabase(newTitle, newDesc);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    public void updateProjectInDatabase(String projectName, String projectDescription){
            if (projectId == null) return;

            binding.progressLoaderPD.setVisibility(View.VISIBLE);

            firestore.collection(Collections.PROJECTS)
                    .document(projectId)
                    .update(Collections.PROJECT_NAME, projectName, Collections.PROJECT_DESCRIPTION, projectDescription)
                    .addOnSuccessListener(aVoid -> {
                        binding.progressLoaderPD.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Project updated successfully", Toast.LENGTH_SHORT).show();
                        getProjectDetails();
                    })
                    .addOnFailureListener(e -> {
                        binding.progressLoaderPD.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    private void deleteProjectAndTasks() {
        if (projectId == null) return;

        binding.progressLoaderPD.setVisibility(VISIBLE);

        //  حذف كل المهام المرتبطة بالمشروع
        firestore.collection(Collections.TASKS)
                .whereEqualTo(Collections.PROJECT_ID, projectId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> taskIds = new ArrayList<>();
                    for (var doc : queryDocumentSnapshots.getDocuments()) {
                        taskIds.add(doc.getId());
                    }

                    // حذف كل المهام واحد واحد
                    for (String taskId : taskIds) {
                        firestore.collection(Collections.TASKS).document(taskId).delete();
                    }

                    // بعد حذف المهام، حذف المشروع نفسه
                    firestore.collection(Collections.PROJECTS)
                            .document(projectId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                binding.progressLoaderPD.setVisibility(View.GONE);
                                Toast.makeText(getContext(),
                                        "Project and its tasks deleted successfully",
                                        Toast.LENGTH_SHORT).show();

                                // ارجع لشاشة الهوم
                                getParentFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ProjectsFragment())
                                        .commit();
                            })
                            .addOnFailureListener(e -> {
                                binding.progressLoaderPD.setVisibility(View.GONE);
                                Toast.makeText(getContext(),
                                        "Failed to delete project: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    binding.progressLoaderPD.setVisibility(View.GONE);
                    Toast.makeText(getContext(),
                            "Failed to delete tasks: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
    public String getProjectId() {
        return projectId;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }
}
