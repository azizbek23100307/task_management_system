package org.example.task_managment_system.payload;

import lombok.Builder;

import org.example.task_managment_system.entity.enums.Role;

import java.time.LocalDateTime;
@Builder

public record UserResponseDto(
        Integer id,
        String fullName,
        String email,
        Role role,
        LocalDateTime createdAt
) {
}
