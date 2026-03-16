package com.example.cleardayapplication.domain.model;

public class ProjectMember {

    private String memberId;
    private String userId;
    private String role;
    private String projectId;

    // Empty constructor (required for Firestore)
    public ProjectMember() {}

    // Getters
    public String getMemberId() { return memberId; }
    public String getUserId() { return userId; }
    public String getRole() { return role; }
    public String getProjectId() { return projectId; }


    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
