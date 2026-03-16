package com.example.cleardayapplication.domain.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Task {
    private String taskId;
    private String projectId;
    private String userId;
    private String title;
    private String description;
    private String status;
    private String createdBy;


    // Empty constructor required for Firebase Firestore
    public Task() {
    }

    // Full constructor
    public Task(String taskId, String projectId, String userId, String status, String createdBy, String description, String title) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.userId = userId;
        this.status = status;
        this.createdBy = createdBy;
        this.description = description;
        this.title = title;
    }

    // Getters and Setters
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

}