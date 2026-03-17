package org.example.task_managment_system.service;

import org.example.task_managment_system.payload.ApiResponse;

public interface AdminService {
    ApiResponse getUsers(Integer currentUserId);
    ApiResponse getTasks(int page, int size, Integer currentUserId);
    ApiResponse deleteUser(Integer userId, Integer currentUserId);
}
