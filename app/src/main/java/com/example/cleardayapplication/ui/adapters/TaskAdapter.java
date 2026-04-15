package com.example.cleardayapplication.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ItemTaskBinding;
import com.example.cleardayapplication.domain.model.Task;
import com.example.cleardayapplication.domain.utils.OnItemClicks;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnItemClicks onItemClicks;

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
        return new TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

            // عرض الملفات المرفقة
            binding.attachmentsContainer.removeAllViews();
            if(task.getAttachments() != null){
                for(String base64 : task.getAttachments()){
                    try {
                        byte[] decoded = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

                        ImageView img = new ImageView(itemView.getContext());
                        img.setImageBitmap(bmp);

                        // حجم صغير 60x60 dp
                        int sizeInDp = 60;
                        float scale = itemView.getContext().getResources().getDisplayMetrics().density;
                        int sizeInPx = (int) (sizeInDp * scale + 0.5f);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                sizeInPx,
                                sizeInPx
                        );
                        params.setMargins(4, 4, 4, 4); // مسافة صغيرة بين الصور
                        img.setLayoutParams(params);
                        img.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        binding.attachmentsContainer.addView(img);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            }
        }
    }
