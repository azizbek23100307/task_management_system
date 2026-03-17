package org.example.task_managment_system.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    private String fullName;
    private String email;
    private String phoneNumber;
    private Integer age;
}
