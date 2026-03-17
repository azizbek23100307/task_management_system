package org.example.task_managment_system.controller;

import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse> getSummary(@RequestParam Integer currentUserId) {
        ApiResponse response = dashboardService.getSummary(currentUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
