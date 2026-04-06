package com.example.cleardayapplication.domain.model;

import com.google.firebase.firestore.DocumentId;

import java.util.List;

public class Project {
    private String projectId;
    private String name;
    private String description;
    private String adminId;
    private String status;
    private List<String> projectMembersId;
    private List<String> tasksId;

    private long createAt;

    // Empty constructor (required for Firestore)
    public Project() {}

    // Getters
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAdminId() { return adminId; }
    public String getStatus() { return status; }
    public List<String> getProjectMembersId() { return projectMembersId; }
    public List<String> getTasksId() { return tasksId; }

    public void setCreateAt(long createAt) { this.createAt = createAt; }

    // Setters
    public void setProjectId(String projectId) { this.projectId = projectId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    public void setStatus(String status) { this.status = status; }
    public void setProjectMembersId(List<String> projectMembersId) { this.projectMembersId = projectMembersId; }
    public void setTasksId(List<String> tasksId) { this.tasksId = tasksId; }
    public long getCreateAt() { return createAt; }
}