package org.example.task_managment_system.payload;

import org.example.task_managment_system.entity.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDto(
        Integer id,
        String fullName,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
