package com.example.cleardayapplication.ui.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cleardayapplication.R;
import com.example.cleardayapplication.databinding.ItemInviteUserBinding;
import com.example.cleardayapplication.domain.model.User;
import com.example.cleardayapplication.domain.utils.OnInviteClickListener;

import java.util.List;

public class InviteUserAdapter extends RecyclerView.Adapter<InviteUserAdapter.InviteUserViewHolder> {

    private List<User> userList;
    private final OnInviteClickListener listener;

    public InviteUserAdapter(List<User> userList, OnInviteClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    public void updateList(List<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InviteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInviteUserBinding binding = ItemInviteUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new InviteUserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteUserViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    class InviteUserViewHolder extends RecyclerView.ViewHolder {

        private final ItemInviteUserBinding binding;

        public InviteUserViewHolder(ItemInviteUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            binding.tvUserName.setText(user.getUserName());
            binding.tvEmail.setText(user.getEmail());

            // Load profile image same way as ProfileFragment
            String imageUrl = user.getProfileImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(imageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.ic_account_circle)
                        .into(binding.ivUser);
            } else {
                Glide.with(binding.getRoot().getContext())
                        .load(R.drawable.ic_account_circle)
                        .circleCrop()
                        .into(binding.ivUser);
            }

            binding.taskStatus.setOnClickListener(v -> {
                if (listener != null) listener.onInviteClick(user);
            });
        }
    }
}