package org.example.task_managment_system.payload;

public record DashboardSummaryDto(
        long totalTasks,
        long todoTasks,
        long inProgressTasks,
        long doneTasks,
        long overdueTasks
) {
}
