package org.example.task_managment_system.service;

import org.example.task_managment_system.payload.ApiResponse;

public interface DashboardService {
    ApiResponse getSummary(Integer currentUserId);
}
