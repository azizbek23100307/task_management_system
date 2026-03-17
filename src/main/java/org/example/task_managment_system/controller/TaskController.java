package org.example.task_managment_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.TaskCreateRequest;
import org.example.task_managment_system.payload.TaskStatusUpdateRequest;
import org.example.task_managment_system.payload.TaskUpdateRequest;
import org.example.task_managment_system.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody TaskCreateRequest request,
                                              @RequestParam Integer currentUserId) {
        ApiResponse response = taskService.create(request, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer taskId,
                                              @Valid @RequestBody TaskUpdateRequest request,
                                              @RequestParam Integer currentUserId) {
        ApiResponse response = taskService.update(taskId, request, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Integer taskId,
                                                    @Valid @RequestBody TaskStatusUpdateRequest request,
                                                    @RequestParam Integer currentUserId) {
        ApiResponse response = taskService.updateStatus(taskId, request, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer taskId,
                                               @RequestParam Integer currentUserId) {
        ApiResponse response = taskService.getById(taskId, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getTasks(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam Integer currentUserId) {
        ApiResponse response = taskService.getTasks(page, size, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer taskId,
                                              @RequestParam Integer currentUserId) {
        ApiResponse response = taskService.delete(taskId, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
