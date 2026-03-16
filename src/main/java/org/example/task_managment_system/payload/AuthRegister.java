package org.example.task_managment_system.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegister {
    private String fullName;
    private String phoneNumber;
    private String email;
    private int age;
    private String password;
}
