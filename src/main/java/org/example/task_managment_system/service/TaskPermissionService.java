package org.example.task_managment_system.service;

import org.example.task_managment_system.entity.Task;
import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.exception.ForbiddenOperationException;
import org.springframework.stereotype.Service;

@Service
public class TaskPermissionService {

    public void checkCanRead(User currentUser, Task task) {
        if (isAdmin(currentUser) || isOwner(currentUser, task) || isAssignee(currentUser, task)) {
            return;
        }
        throw new ForbiddenOperationException("Bu taskni ko'rishga ruxsat yo'q");
    }

    public void checkCanUpdate(User currentUser, Task task) {
        if (isAdmin(currentUser) || isOwner(currentUser, task) || isAssignee(currentUser, task)) {
            return;
        }
        throw new ForbiddenOperationException("Bu taskni o'zgartirishga ruxsat yo'q");
    }

    public void checkCanDelete(User currentUser, Task task) {
        if (isAdmin(currentUser) || isOwner(currentUser, task)) {
            return;
        }
        throw new ForbiddenOperationException("Bu taskni o'chirishga ruxsat yo'q");
    }

    public void checkCanAssign(User currentUser, User assignee) {
        if (isAdmin(currentUser)) {
            return;
        }

        if (!currentUser.getId().equals(assignee.getId())) {
            throw new ForbiddenOperationException("Oddiy user taskni faqat o'ziga assign qila oladi");
        }
    }

    private boolean isAdmin(User user) {
        return user.getRole() == Role.ROLE_ADMIN;
    }

    private boolean isOwner(User user, Task task) {
        return task.getCreatedBy() != null && task.getCreatedBy().equals(user.getId());
    }

    private boolean isAssignee(User user, Task task) {
        return task.getAssignedTo() != null && task.getAssignedTo().getId().equals(user.getId());
    }
}
