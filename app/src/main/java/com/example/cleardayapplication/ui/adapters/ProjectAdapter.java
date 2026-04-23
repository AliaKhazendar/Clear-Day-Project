package com.example.cleardayapplication.ui.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ItemProjectBinding;
import com.example.cleardayapplication.domain.model.Project;
import com.example.cleardayapplication.domain.utils.OnItemClicks;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Project> projectList;
    private final OnItemClicks listener;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ProjectAdapter(List<Project> projectList, OnItemClicks listener) {
        this.projectList = projectList;
        this.listener = listener;
    }

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

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        private final ItemProjectBinding binding;

        public ProjectViewHolder(@NonNull ItemProjectBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Project project) {
            binding.tvProjectTitle.setText(project.getName());
            binding.tvProjectDescription.setText(project.getDescription());

            int color;
            String statusText;
            if ("open".equalsIgnoreCase(project.getStatus())) {
                statusText = itemView.getContext().getString(R.string.status_open);
                color = ContextCompat.getColor(itemView.getContext(), R.color.green);
            } else {
                statusText = itemView.getContext().getString(R.string.status_closed);
                color = ContextCompat.getColor(itemView.getContext(), R.color.red);
            }

            binding.tvProjectStatus.setText(statusText);
            Drawable background = binding.tvProjectStatus.getBackground();
            if (background != null) {
                background.setTint(color);
            }

            fetchAdminName(project.getAdminId());

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onCardItemClick(project);
            });
        }

        private void fetchAdminName(String adminId) {
            if (adminId == null || adminId.isEmpty()) {
                binding.tvAdminName.setText(itemView.getContext().getString(R.string.unknown));
                return;
            }

            firestore.collection("User")
                    .document(adminId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String adminName = documentSnapshot.getString("userName");
                            binding.tvAdminName.setText(adminName != null ? adminName : itemView.getContext().getString(R.string.unknown));
                        } else {
                            binding.tvAdminName.setText(itemView.getContext().getString(R.string.unknown));
                        }
                    })
                    .addOnFailureListener(e ->
                            binding.tvAdminName.setText(itemView.getContext().getString(R.string.unknown)));
        }
    }
}