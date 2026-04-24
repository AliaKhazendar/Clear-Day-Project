package com.example.cleardayapplication.domain.utils;

import com.example.cleardayapplication.domain.model.Task;

public interface OnTaskEditedListener {
    void onTaskEdited(String taskID);
    void onTaskDeleted(String taskID);
}
