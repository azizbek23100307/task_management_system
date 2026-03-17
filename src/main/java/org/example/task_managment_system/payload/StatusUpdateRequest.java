package org.example.task_managment_system.payload;

import jakarta.validation.constraints.NotNull;
import org.example.task_managment_system.entity.enums.TaskStatus;

public record StatusUpdateRequest(
        @NotNull(message = "Status bo'sh bo'lmasligi kerak")
        TaskStatus status
) {
}
