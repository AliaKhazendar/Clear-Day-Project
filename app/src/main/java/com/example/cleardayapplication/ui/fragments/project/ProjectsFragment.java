package com.example.cleardayapplication.ui.fragments.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.FragmentProjectsBinding;
import com.example.cleardayapplication.domain.model.Project;
import com.example.cleardayapplication.domain.utils.OnItemClicks;
import com.example.cleardayapplication.ui.adapters.ProjectAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment implements OnItemClicks {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private FragmentProjectsBinding binding;
    private ProjectAdapter adapter;
    private final List<Project> projectList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;


    public ProjectsFragment() {
        // Required empty public constructor
    }

    public static ProjectsFragment newInstance(String param1, String param2) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        setupRecyclerView();
        loadCurrentUserProjects();

        return binding.getRoot();

    }
    // ── Setup RecyclerView ───────────────────────────────────
    private void setupRecyclerView() {
        adapter = new ProjectAdapter(projectList, this);

        binding.rvProjects.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.rvProjects.setAdapter(adapter);
    }

    private void loadCurrentUserProjects() {

        String currentUserId = auth.getCurrentUser().getUid();

        binding.progressLoader.setVisibility(View.VISIBLE);

        firestore.collection("Project")
                .whereArrayContains("projectMembersId", currentUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    binding.progressLoader.setVisibility(View.GONE);

                    if (!querySnapshot.isEmpty()) {
                        List<Project> list = querySnapshot.toObjects(Project.class);
                        adapter.updateList(list);
                        binding.tvEmptyState.setVisibility(View.GONE);
                    } else {
                        binding.tvEmptyState.setVisibility(View.VISIBLE);
                        binding.tvEmptyState.setText("No projects found");
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressLoader.setVisibility(View.GONE);
                    Toast.makeText(getContext(),
                            "Failed to load projects: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onCardItemClick(Object obj) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,ProjectDetailsFragment.newInstance(obj))
                .commit();
    }
}