package org.example.task_managment_system.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}