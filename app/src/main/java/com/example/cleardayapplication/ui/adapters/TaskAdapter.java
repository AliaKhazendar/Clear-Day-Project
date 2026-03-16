package com.example.cleardayapplication.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        return !taskList.isEmpty()? taskList.size():0;
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

            if ("open".equalsIgnoreCase(task.getStatus())) {
                binding.taskStatus.setText(R.string.status_open);
                binding.taskStatus.getBackground()
                        .setTint(Color.parseColor("#4CAF50")); // green
            } else {
                binding.taskStatus.setText(R.string.status_closed);
                binding.taskStatus.getBackground()
                        .setTint(Color.parseColor("#F44336")); // red
            }
        }
    }
}
