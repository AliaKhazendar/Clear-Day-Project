package com.example.cleardayapplication.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ActivityHomeBinding;
import com.example.cleardayapplication.domain.utils.Collections;
import com.example.cleardayapplication.domain.utils.OnGoToInvitePeopleListener;
import com.example.cleardayapplication.domain.utils.OnTaskEditedListener;
import com.example.cleardayapplication.ui.fragments.invitation.InvitationFragment;
import com.example.cleardayapplication.ui.fragments.invitation.SendInvitationFragment;
import com.example.cleardayapplication.ui.fragments.profile.ProfileFragment;
import com.example.cleardayapplication.ui.fragments.project.AddProjectFragment;
import com.example.cleardayapplication.ui.fragments.project.ProjectDetailsFragment;
import com.example.cleardayapplication.ui.fragments.project.ProjectsFragment;
import com.example.cleardayapplication.ui.fragments.task.AddTaskFragment;
import com.example.cleardayapplication.ui.fragments.task.EditTasksInformationFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity implements OnGoToInvitePeopleListener, OnTaskEditedListener {
    public ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // مراقبة الـ BackStack لتحديث حالة الزر تلقائياً عند العودة
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            updateFabVisibility();
        });

        binding.ivInvitation.setVisibility(View.VISIBLE);
        binding.ivInvitation.setOnClickListener(view -> {
            navigateFragment(new InvitationFragment());
            binding.ivInvitation.setVisibility(View.GONE);
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ProjectsFragment())
                    .commit();
        }

        binding.navHome.setOnClickListener(item -> {
            navigateFragment(new ProjectsFragment());
        });

        binding.navProfile.setOnClickListener(item -> {
            navigateFragment(new ProfileFragment());
        });

        binding.fabAdd.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            
            if (currentFragment instanceof ProjectsFragment) {
                AddProjectFragment dialog = new AddProjectFragment();
                dialog.setOnProjectAddedListener(() -> {
                    ((ProjectsFragment) currentFragment).loadCurrentUserProjects();
                });
                dialog.show(getSupportFragmentManager(), "AddProjectDialog");
            } else if (currentFragment instanceof ProjectDetailsFragment) {
                ProjectDetailsFragment projectFragment = (ProjectDetailsFragment) currentFragment;
                String projectId = projectFragment.getProjectId();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                AddTaskFragment fragment = AddTaskFragment.newInstance(projectId, userId);
                navigateFragment(fragment);
            }
        });
    }

    private void updateFabVisibility() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        binding.fabAdd.setEnabled(true);
        binding.fabAdd.setVisibility(View.VISIBLE);

        if (currentFragment instanceof ProfileFragment || 
            currentFragment instanceof InvitationFragment || 
            currentFragment instanceof AddTaskFragment ||
            currentFragment instanceof SendInvitationFragment) {
            binding.fabAdd.setVisibility(View.GONE);
        }
    }

    private void navigateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
        
        // تحديث الرؤية فوراً بعد الطلب
        binding.getRoot().postDelayed(this::updateFabVisibility, 100);
    }

    @Override
    public void onGoToInvitePeople(String projectId) {
        navigateFragment(SendInvitationFragment.newInstance(projectId));
    }

    @Override
    public void onTaskEdited(String taskID) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ProjectDetailsFragment) {
            EditTasksInformationFragment dialog = EditTasksInformationFragment.newInstance(taskID);
            dialog.setOnTakEditedListener(() -> {
                ((ProjectDetailsFragment) currentFragment).loadProjectAndTasks();
            });
            dialog.show(getSupportFragmentManager(), "EditTaskDialog");
        }
    }

    @Override
    public void onTaskDeleted(String taskID) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ProjectDetailsFragment) {
            ProjectDetailsFragment detailsFragment = (ProjectDetailsFragment) currentFragment;

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection(Collections.TASKS)
                                .document(taskID)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
                                    detailsFragment.loadProjectAndTasks();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error deleting task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
}
