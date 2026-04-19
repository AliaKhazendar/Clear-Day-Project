package com.example.cleardayapplication.ui.fragments.invitation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cleardayapplication.R;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cleardayapplication.databinding.FragmentSendInvitationBinding;

import com.example.cleardayapplication.domain.model.User;
import com.example.cleardayapplication.ui.adapters.InviteUserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendInvitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendInvitationFragment extends Fragment {


    private static final String ARG_PROJECT_ID = "projectId";

    private FragmentSendInvitationBinding binding;

    private String projectId;
    private InviteUserAdapter adapter;
    private final List<User> userList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public SendInvitationFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SendInvitationFragment newInstance(String projectId) {
        SendInvitationFragment fragment = new SendInvitationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            projectId = getArguments().getString(ARG_PROJECT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSendInvitationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupSearchView();
    }
    private void setupRecyclerView() {
        adapter = new InviteUserAdapter(userList, this::sendInvitation);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUsers.setAdapter(adapter);
    }
    private void setupSearchView() {
        binding.svUsers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUsersByEmail(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() >= 3) {
                    searchUsersByEmail(newText.trim());
                } else {
                    userList.clear();
                    adapter.updateList(new ArrayList<>());
                }
                return true;
            }
        });
    }
    private void searchUsersByEmail(String emailQuery) {
        if (emailQuery.isEmpty()) return;

        String currentUserId = auth.getCurrentUser() != null
                ? auth.getCurrentUser().getUid() : "";

        String end = emailQuery + "\uf8ff";

        db.collection("User")
                .whereGreaterThanOrEqualTo("email", emailQuery)
                .whereLessThanOrEqualTo("email", end)
                .get()
                .addOnSuccessListener(snapshots -> {
                    List<User> results = new ArrayList<>();
                    snapshots.forEach(doc -> {
                        User user = doc.toObject(User.class);
                        user.setUserId(doc.getId());
                        if (!doc.getId().equals(currentUserId)) {
                            results.add(user);
                        }
                    });
                    adapter.updateList(results);

                    if (results.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "No users found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Search failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
    private void sendInvitation(User invitedUser) {
        String adminId = auth.getCurrentUser() != null
                ? auth.getCurrentUser().getUid() : "";

        if (projectId == null || projectId.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Project ID missing", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Invitation")
                .whereEqualTo("ProjectId", projectId)
                .whereEqualTo("InvitedUserId", invitedUser.getUserId())
                .get()
                .addOnSuccessListener(existing -> {
                    if (!existing.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Invitation already sent to " + invitedUser.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Map<String, Object> invitation = new HashMap<>();
                    invitation.put("AdminId",       adminId);
                    invitation.put("InvitedUserId", invitedUser.getUserId());
                    invitation.put("IsAccepted",    false);
                    invitation.put("ProjectId",     projectId);

                    db.collection("Invitation")
                            .add(invitation)
                            .addOnSuccessListener(ref ->
                                    Toast.makeText(requireContext(),
                                            "Invitation sent to " + invitedUser.getEmail(),
                                            Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(requireContext(),
                                            "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                })
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Check failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}