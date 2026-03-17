package org.example.task_managment_system.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.entity.Task;
import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.entity.enums.TaskPriority;
import org.example.task_managment_system.entity.enums.TaskStatus;
import org.example.task_managment_system.exception.BadRequestException;
import org.example.task_managment_system.exception.ResourceNotFoundException;
import org.example.task_managment_system.mapper.TaskMapper;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.TaskCreateRequest;
import org.example.task_managment_system.payload.TaskStatusUpdateRequest;
import org.example.task_managment_system.payload.TaskUpdateRequest;
import org.example.task_managment_system.repository.TaskRepository;
import org.example.task_managment_system.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskPermissionService taskPermissionService;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public ApiResponse create(TaskCreateRequest request, Integer currentUserId) {
        User currentUser = getUserOrThrow(currentUserId);

        validateDueDate(request.dueDate());
        User assignee = resolveAssignee(request.assignedToId(), currentUser);

        Task task = Task.builder()
                .title(cleanText(request.title()))
                .description(cleanNullableText(request.description()))
                .status(TaskStatus.TODO)
                .priority(defaultPriority(request.priority()))
                .dueDate(request.dueDate())
                .assignedTo(assignee)
                .build();

        Task savedTask = taskRepository.save(task);

        return ApiResponse.builder()
                .message("Task muvaffaqiyatli yaratildi")
                .success(true)
                .status(HttpStatus.CREATED)
                .body(taskMapper.toResponse(savedTask))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse update(Integer taskId, TaskUpdateRequest request, Integer currentUserId) {
        User currentUser = getUserOrThrow(currentUserId);
        Task task = findTaskOrThrow(taskId);

        taskPermissionService.checkCanUpdate(currentUser, task);
        validateDueDate(request.dueDate());

        User assignee = resolveAssignee(request.assignedToId(), currentUser);

        task.setTitle(cleanText(request.title()));
        task.setDescription(cleanNullableText(request.description()));
        task.setPriority(defaultPriority(request.priority()));
        task.setDueDate(request.dueDate());
        task.setAssignedTo(assignee);

        return ApiResponse.builder()
                .message("Task muvaffaqiyatli yangilandi")
                .success(true)
                .status(HttpStatus.OK)
                .body(taskMapper.toResponse(task))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse updateStatus(Integer taskId, TaskStatusUpdateRequest request, Integer currentUserId) {
        User currentUser = getUserOrThrow(currentUserId);
        Task task = findTaskOrThrow(taskId);

        taskPermissionService.checkCanUpdate(currentUser, task);
        validateStatusTransition(task.getStatus(), request.status());

        task.setStatus(request.status());

        return ApiResponse.builder()
                .message("Task status muvaffaqiyatli yangilandi")
                .success(true)
                .status(HttpStatus.OK)
                .body(taskMapper.toResponse(task))
                .build();
    }

    @Override
    public ApiResponse getById(Integer taskId, Integer currentUserId) {
        User currentUser = getUserOrThrow(currentUserId);
        Task task = findTaskOrThrow(taskId);

        taskPermissionService.checkCanRead(currentUser, task);

        return ApiResponse.builder()
                .message("Task topildi")
                .success(true)
                .status(HttpStatus.OK)
                .body(taskMapper.toResponse(task))
                .build();
    }

    @Override
    public ApiResponse getTasks(int page, int size, Integer currentUserId) {
        User currentUser = getUserOrThrow(currentUserId);
        Pageable pageable = PageRequest.of(page, size);

        Page<?> taskPage;

        if (currentUser.getRole() == Role.ROLE_ADMIN) {
            taskPage = taskRepository.findAllWithAssignedTo(pageable)
                    .map(taskMapper::toResponse);
        } else {
            taskPage = taskRepository.findAccessibleTasks(currentUser.getId(), pageable)
                    .map(taskMapper::toResponse);
        }

        return ApiResponse.builder()
                .message("Tasklar ro'yxati")
                .success(true)
                .status(HttpStatus.OK)
                .body(taskPage)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse delete(Integer taskId, Integer currentUserId) {
        User currentUser = getUserOrThrow(currentUserId);
        Task task = findTaskOrThrow(taskId);

        taskPermissionService.checkCanDelete(currentUser, task);
        taskRepository.delete(task);

        return ApiResponse.builder()
                .message("Task muvaffaqiyatli o'chirildi")
                .success(true)
                .status(HttpStatus.OK)
                .body(null)
                .build();
    }

    private User getUserOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi: " + userId));
    }

    private Task findTaskOrThrow(Integer taskId) {
        return taskRepository.findByIdWithAssignedTo(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task topilmadi: " + taskId));
    }

    private User resolveAssignee(Integer assignedToId, User currentUser) {
        if (assignedToId == null) {
            return null;
        }

        User assignee = userRepository.findById(assignedToId)
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user topilmadi: " + assignedToId));

        taskPermissionService.checkCanAssign(currentUser, assignee);
        return assignee;
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Due date o'tgan sana bo'lmasligi kerak");
        }
    }

    private void validateStatusTransition(TaskStatus currentStatus, TaskStatus newStatus) {
        if (currentStatus == TaskStatus.DONE && newStatus == TaskStatus.TODO) {
            throw new BadRequestException("DONE bo'lgan taskni to'g'ridan-to'g'ri TODO ga qaytarib bo'lmaydi");
        }
    }

    private TaskPriority defaultPriority(TaskPriority priority) {
        return priority == null ? TaskPriority.MEDIUM : priority;
    }

    private String cleanText(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException("Matn bo'sh bo'lmasligi kerak");
        }
        return value.trim();
    }

    private String cleanNullableText(String value) {
        return value == null ? null : value.trim();
    }
}
