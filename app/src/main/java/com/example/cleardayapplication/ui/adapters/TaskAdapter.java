package com.example.cleardayapplication.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ItemTaskBinding;
import com.example.cleardayapplication.domain.model.Task;
import com.example.cleardayapplication.domain.utils.OnItemClicks;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    List<Task> taskList;
    OnItemClicks onItemClicks;

    public TaskAdapter(List<Task> taskList, OnItemClicks onItemClicks){
        this.taskList = taskList;
        this.onItemClicks = onItemClicks;
    }
    public void updateList(List<Task> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder
                (ItemTaskBinding.inflate
                (LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskList.get(position));
    }


    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private final ItemTaskBinding binding;
        public TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Task task){
            binding.taskTitle.setText(task.getTitle());
            binding.taskDesc.setText(task.getDescription());

            int color;
            String status = task.getStatus();

            if ("Urgent".equalsIgnoreCase(status)) {
                binding.taskStatus.setText("Urgent");
                color = ContextCompat.getColor(itemView.getContext(), R.color.red);
            } else if ("Running".equalsIgnoreCase(status)) {
                binding.taskStatus.setText("Running");
                color = ContextCompat.getColor(itemView.getContext(), R.color.green);
            } else if ("Ongoing".equalsIgnoreCase(status)) {
                binding.taskStatus.setText("Ongoing");
                color = ContextCompat.getColor(itemView.getContext(), R.color.purple);
            } else {
                binding.taskStatus.setText(status != null ? status : "");
                color = ContextCompat.getColor(itemView.getContext(), R.color.light_gray);
            }

            if (binding.taskStatus.getBackground() != null) {
                binding.taskStatus.getBackground().setTint(color);
            }
        }
    }
}
