package org.example.task_managment_system.service;

import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.entity.enums.TaskStatus;
import org.example.task_managment_system.exception.ResourceNotFoundException;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.DashboardSummaryDto;
import org.example.task_managment_system.repository.TaskRepository;
import org.example.task_managment_system.repository.UserRepository;
import org.example.task_managment_system.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public ApiResponse getSummary(Integer currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi: " + currentUserId));

        DashboardSummaryDto dto;

        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            dto = new DashboardSummaryDto(
                    taskRepository.countAllTasks(),
                    taskRepository.countAllByStatus(TaskStatus.TODO),
                    taskRepository.countAllByStatus(TaskStatus.IN_PROGRESS),
                    taskRepository.countAllByStatus(TaskStatus.DONE),
                    taskRepository.countAllOverdue(LocalDate.now())
            );
        } else {
            dto = new DashboardSummaryDto(
                    taskRepository.countAccessibleTasks(currentUserId),
                    taskRepository.countAccessibleTasksByStatus(currentUserId, TaskStatus.TODO),
                    taskRepository.countAccessibleTasksByStatus(currentUserId, TaskStatus.IN_PROGRESS),
                    taskRepository.countAccessibleTasksByStatus(currentUserId, TaskStatus.DONE),
                    taskRepository.countAccessibleOverdueTasks(currentUserId, LocalDate.now())
            );
        }

        return ApiResponse.builder()
                .message("Dashboard summary")
                .success(true)
                .status(HttpStatus.OK)
                .body(dto)
                .build();
    }
}
