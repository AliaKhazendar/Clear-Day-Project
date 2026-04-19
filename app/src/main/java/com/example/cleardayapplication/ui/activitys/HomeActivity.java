package com.example.cleardayapplication.ui.activitys;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ActivityHomeBinding;
import com.example.cleardayapplication.ui.fragments.profile.*;
import com.example.cleardayapplication.ui.fragments.project.AddProjectFragment;
import com.example.cleardayapplication.ui.fragments.project.ProjectDetailsFragment;
import com.example.cleardayapplication.ui.fragments.project.ProjectsFragment;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
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
        navigateFragment(new ProjectsFragment());

        binding.navHome.setOnClickListener( item ->{
            navigateFragment(new ProjectsFragment());
        });
        binding.navProfile.setOnClickListener( item ->{
            navigateFragment(new ProfileFragment());
        });

        // floating action button
        binding.fabAdd.setOnClickListener(v->{
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(currentFragment instanceof ProjectsFragment){
                AddProjectFragment dialog = new AddProjectFragment();
                dialog.setOnProjectAddedListener(() -> {
                    ((ProjectsFragment) currentFragment).loadCurrentUserProjects();
                });
                dialog.show(getSupportFragmentManager(), "AddProjectDialog");
            }else if(currentFragment instanceof ProjectDetailsFragment){
                // add task
            }else{
                binding.fabAdd.setEnabled(false);
            }
        });
    }

    private void navigateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}