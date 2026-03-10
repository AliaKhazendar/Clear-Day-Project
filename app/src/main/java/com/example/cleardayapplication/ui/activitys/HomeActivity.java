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

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ProjectsFragment())
                .addToBackStack(null)
                .commit();

        binding.bottomNavigation.setOnItemSelectedListener( item ->{
            if (item.getItemId() == R.id.nav_home) {
                navigateFragment(new ProjectsFragment());
            } else if (item.getItemId() == R.id.nav_account) {

            }
            return true;
        });

        // floating action button
        binding.fabAdd.setOnClickListener(v->{
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(currentFragment instanceof ProjectsFragment){
                // add project
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