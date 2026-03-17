package org.example.task_managment_system.payload;

import org.example.task_managment_system.entity.enums.TaskPriority;
import org.example.task_managment_system.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(Integer id,
                           String title,
                           String description,
                           TaskStatus status,
                           TaskPriority priority,
                           LocalDate dueDate,
                           Integer createdBy,
                           Integer updatedBy,
                           Integer assignedToId,
                           String assignedToName,
                           LocalDateTime createdAt,
                           LocalDateTime updatedAt) {
}
