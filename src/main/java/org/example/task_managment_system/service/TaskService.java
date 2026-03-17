package org.example.task_managment_system.service;

import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.TaskCreateRequest;
import org.example.task_managment_system.payload.TaskStatusUpdateRequest;
import org.example.task_managment_system.payload.TaskUpdateRequest;


public interface TaskService {
    ApiResponse create(TaskCreateRequest request, Integer currentUserId);
    ApiResponse update(Integer taskId, TaskUpdateRequest request, Integer currentUserId);
    ApiResponse updateStatus(Integer taskId, TaskStatusUpdateRequest request, Integer currentUserId);
    ApiResponse getById(Integer taskId, Integer currentUserId);
    ApiResponse getTasks(int page, int size, Integer currentUserId);
    ApiResponse delete(Integer taskId, Integer currentUserId);
}
