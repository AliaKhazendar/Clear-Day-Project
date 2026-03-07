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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment implements OnItemClicks {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentProjectsBinding binding;
    private ProjectAdapter adapter;
    private final List<Project> projectList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    }
}