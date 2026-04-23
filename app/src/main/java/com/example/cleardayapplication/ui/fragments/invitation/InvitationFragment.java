package com.example.cleardayapplication.ui.fragments.invitation;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cleardayapplication.databinding.FragmentInvitationBinding;
import com.example.cleardayapplication.domain.model.Invitation;
import com.example.cleardayapplication.ui.adapters.InvitationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class InvitationFragment extends Fragment {

    private FragmentInvitationBinding binding;
    private InvitationAdapter adapter;
    private final List<Invitation> invitationList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db   = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInvitationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadInvitations();
    }

    private void setupRecyclerView() {
        adapter = new InvitationAdapter(invitationList, this::acceptInvitation);
        binding.rvInvitations.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInvitations.setAdapter(adapter);
    }

    // Load only invitations where IsAccepted = false for the current user
    private void loadInvitations() {
        String currentUserId = auth.getCurrentUser() != null
                ? auth.getCurrentUser().getUid() : "";

        binding.progressLoader.setVisibility(View.VISIBLE);

        db.collection("Invitation")
                .whereEqualTo("InvitedUserId", currentUserId)
                .whereEqualTo("IsAccepted", false)
                .get()
                .addOnSuccessListener(snapshots -> {
                    invitationList.clear();

                    // For each invitation, fetch the project name to display it
                    List<QueryDocumentSnapshot> docs = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshots) docs.add(doc);

                    if (docs.isEmpty()) {
                        binding.progressLoader.setVisibility(View.GONE);
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        adapter.updateList(invitationList);
                        return;
                    }

                    // Counter to know when all project names are fetched
                    final int[] counter = {0};

                    for (QueryDocumentSnapshot doc : docs) {
                        Invitation invitation = new Invitation();
                        invitation.setInvitationId(doc.getId());
                        invitation.setAdminId(doc.getString("AdminId"));
                        invitation.setInvitedUserId(doc.getString("InvitedUserId"));
                        invitation.setProjectId(doc.getString("ProjectId"));
                        invitation.setAccepted(Boolean.TRUE.equals(doc.getBoolean("IsAccepted")));

                        // Fetch project name for display
                        db.collection("Project")
                                .document(invitation.getProjectId())
                                .get()
                                .addOnSuccessListener(projectDoc -> {
                                    if (projectDoc.exists()) {
                                        invitation.setProjectName(projectDoc.getString("name"));
                                    }
                                    invitationList.add(invitation);
                                    counter[0]++;

                                    // When all fetches are done, update the list
                                    if (counter[0] == docs.size()) {
                                        binding.progressLoader.setVisibility(View.GONE);
                                        binding.tvEmpty.setVisibility(View.GONE);
                                        adapter.updateList(invitationList);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    counter[0]++;
                                    if (counter[0] == docs.size()) {
                                        binding.progressLoader.setVisibility(View.GONE);
                                        adapter.updateList(invitationList);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressLoader.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            "Failed to load invitations: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void acceptInvitation(Invitation invitation) {
        binding.progressLoader.setVisibility(View.VISIBLE);

        // Step 1: Update IsAccepted = true in Invitation collection
        db.collection("Invitation")
                .document(invitation.getInvitationId())
                .update("IsAccepted", true)
                .addOnSuccessListener(unused ->

                        // Step 2: Add current user to projectMembersId array in Project collection
                        db.collection("Project")
                                .document(invitation.getProjectId())
                                .update("projectMembersId",
                                        FieldValue.arrayUnion(invitation.getInvitedUserId()))
                                .addOnSuccessListener(unused2 -> {
                                    binding.progressLoader.setVisibility(View.GONE);
                                    Toast.makeText(requireContext(),
                                            "You joined " + invitation.getProjectName() + "!",
                                            Toast.LENGTH_SHORT).show();

                                    // Remove from list after accepting
                                    invitationList.remove(invitation);
                                    adapter.updateList(invitationList);

                                    if (invitationList.isEmpty()) {
                                        binding.tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    binding.progressLoader.setVisibility(View.GONE);
                                    Toast.makeText(requireContext(),
                                            "Failed to join project: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                })
                )
                .addOnFailureListener(e -> {
                    binding.progressLoader.setVisibility(View.GONE);
                    Toast.makeText(requireContext(),
                            "Failed to accept: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}