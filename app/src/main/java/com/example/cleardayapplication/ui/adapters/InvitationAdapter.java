package com.example.cleardayapplication.ui.adapters;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleardayapplication.databinding.ItemInvitationBinding;
import com.example.cleardayapplication.domain.model.Invitation;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {

    public interface OnAcceptClickListener {
        void onAcceptClick(Invitation invitation);
    }

    private List<Invitation> invitationList;
    private final OnAcceptClickListener listener;

    public InvitationAdapter(List<Invitation> invitationList, OnAcceptClickListener listener) {
        this.invitationList = invitationList;
        this.listener = listener;
    }

    public void updateList(List<Invitation> newList) {
        this.invitationList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInvitationBinding binding = ItemInvitationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new InvitationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {
        holder.bind(invitationList.get(position));
    }

    @Override
    public int getItemCount() {
        return invitationList != null ? invitationList.size() : 0;
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder {
        private final ItemInvitationBinding binding;

        public InvitationViewHolder(ItemInvitationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Invitation invitation) {
            binding.tvProjectName.setText(invitation.getProjectName());
            binding.tvInvitedBy.setText("Invited by admin");

            binding.btnAccept.setOnClickListener(v -> {
                if (listener != null) listener.onAcceptClick(invitation);
            });
        }
    }
}
