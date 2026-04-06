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
    private String date;
    private String startTime;
    private String endTime;


    // Empty constructor required for Firebase Firestore
    public Task() {
    }

    // Full constructor
    public Task(String taskId, String projectId, String userId, String title, String description, String status, String createdBy, String date, String startTime, String endTime) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}