package org.example.task_managment_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUsers(@RequestParam Integer currentUserId) {
        ApiResponse response = adminService.getUsers(currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse> getTasks(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam Integer currentUserId) {
        ApiResponse response = adminService.getTasks(page, size, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId,
                                                  @RequestParam Integer currentUserId) {
        ApiResponse response = adminService.deleteUser(userId, currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
