package org.example.task_managment_system.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
      @NotBlank(message = "Full name bo'sh bo'lmasligi kerak")
      String fullName,

      @NotBlank(message = "Email bo'sh bo'lmasligi kerak")
      @Email(message = "Email format noto'g'ri")
      String email) {
}
