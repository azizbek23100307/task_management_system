package org.example.task_managment_system.component;

import lombok.RequiredArgsConstructor;

import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (ddl.equals("create") || ddl.equals("create-drop")) {
            User user = new User();
            user.setFullName("Admin admin");
            user.setAge(20);
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole(Role.ROLE_ADMIN);
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
