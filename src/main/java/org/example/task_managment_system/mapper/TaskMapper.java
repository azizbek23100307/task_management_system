package org.example.task_managment_system.mapper;

import org.example.task_managment_system.entity.Task;
import org.example.task_managment_system.payload.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        Integer assignedToId = null;
        String assignedToName = null;

        if (task.getAssignedTo() != null) {
            assignedToId = task.getAssignedTo().getId();
            assignedToName = task.getAssignedTo().getFullName();
        }

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedBy(),
                task.getUpdatedBy(),
                assignedToId,
                assignedToName,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
