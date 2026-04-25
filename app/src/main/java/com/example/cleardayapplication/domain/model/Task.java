package com.example.cleardayapplication.domain.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Task {

    private String taskId;
    private String title;
    private String description;
    private String projectId;
    private String userId;
    private String status;
    private String createdBy;
    private List<String> attachments;
    private Object date;
    private Object startTime;
    private Object endTime;

    public Task() {} // required for Firestore

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getProjectId() { return projectId; }
    public void setProjectId(String projectId) { this.projectId = projectId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }

    public Object getDate() { return date; }
    public void setDate(Object date) { this.date = date; }

    public Object getStartTime() { return startTime; }
    public void setStartTime(Object startTime) { this.startTime = startTime; }

    public Object getEndTime() { return endTime; }
    public void setEndTime(Object endTime) { this.endTime = endTime; }
}