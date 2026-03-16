package com.example.cleardayapplication.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cleardayapplication.databinding.ItemProjectBinding;
import com.example.cleardayapplication.domain.model.Project;
import com.example.cleardayapplication.domain.utils.OnItemClicks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projectList;
    private final OnItemClicks listener;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    // ── Constructor ──────────────────────────────────────────
    public ProjectAdapter(List<Project> projectList, OnItemClicks listener) {
        this.projectList = projectList;
        this.listener = listener;
    }

    // ── Adapter Methods ──────────────────────────────────────
    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectBinding binding = ItemProjectBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProjectViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.bind(projectList.get(position));
    }

    @Override
    public int getItemCount() {
        return projectList != null ? projectList.size() : 0;
    }

    public void updateList(List<Project> newList) {
        this.projectList = newList;
        notifyDataSetChanged();
    }

    // ── ViewHolder ───────────────────────────────────────────
    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final ItemProjectBinding binding;

        public ProjectViewHolder(@NonNull ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Project project) {

            // Project name
            binding.tvProjectTitle.setText(project.getName());

            // Description
            binding.tvProjectDescription.setText(project.getDescription());

            // Status badge
            if ("open".equalsIgnoreCase(project.getStatus())) {
                binding.tvProjectStatus.setText("Open");
                binding.tvProjectStatus.getBackground()
                        .setTint(Color.parseColor("#4CAF50")); // green
            } else {
                binding.tvProjectStatus.setText("Closed");
                binding.tvProjectStatus.getBackground()
                        .setTint(Color.parseColor("#F44336")); // red
            }

            fetchAdminName(project.getAdminId());

            // Click listener
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onCardItemClick(project);
            });
        }

        private void fetchAdminName(String adminId) {

            if (adminId == null || adminId.isEmpty()) {
                binding.tvAdminName.setText("Unknown");
                return;
            }

            firestore.collection("User")
                    .document(adminId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String adminName = documentSnapshot.getString("userName");
                            binding.tvAdminName.setText(
                                    adminName != null ? adminName : "Unknown");
                        } else {
                            binding.tvAdminName.setText("Unknown");
                        }
                    })
                    .addOnFailureListener(e ->
                            binding.tvAdminName.setText("Unknown"));
        }
    }
}