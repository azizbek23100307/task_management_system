package org.example.task_managment_system.payload;


import jakarta.validation.constraints.NotBlank;
import org.example.task_managment_system.entity.enums.TaskPriority;

import java.time.LocalDate;

public record TaskCreateRequest(
        @NotBlank(message = "Title bo'sh bo'lmasligi kerak")
        String title,
        String description,
        TaskPriority priority,
        LocalDate dueDate,
        Integer assignedToId
) {
}
