package org.example.task_managment_system.service;


import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.AuthLogin;
import org.example.task_managment_system.payload.AuthRegister;
import org.example.task_managment_system.payload.ChangePasswordDto;
import org.example.task_managment_system.payload.UserUpdateDto;
import org.example.task_managment_system.payload.UserResponseDto;
import org.example.task_managment_system.repository.UserRepository;
import org.example.task_managment_system.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public ApiResponse registerUser(AuthRegister authRegister) {
        boolean exists = userRepository.existsByEmail(authRegister.getEmail());
        if (exists) {
            return new ApiResponse("User already exists", false, HttpStatus.ALREADY_REPORTED, null);
        }

        User user = User.builder()
                .email(authRegister.getEmail().trim())
                .fullName(authRegister.getFullName().trim())
                .age(authRegister.getAge())
                .phoneNumber(authRegister.getPhoneNumber())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        return new ApiResponse("User registered", true, HttpStatus.OK, null);
    }

    public ApiResponse login(AuthLogin authLogin) {
        User user = userRepository.findByEmail(authLogin.getEmail()).orElse(null);

        if (user == null) {
            return new ApiResponse("User not found", false, HttpStatus.NOT_FOUND, null);
        }

        if (!user.isEnabled()) {
            return new ApiResponse("User is disabled", false, HttpStatus.FORBIDDEN, null);
        }

        if (passwordEncoder.matches(authLogin.getPassword(), user.getPassword())) {
            String token = jwtProvider.generateToken(user.getEmail());
            return new ApiResponse("Success", true, HttpStatus.OK, token);
        }

        return new ApiResponse("Wrong password", false, HttpStatus.BAD_REQUEST, null);
    }

    public ApiResponse getMe(User currentUser) {
        UserResponseDto dto = mapToDto(currentUser);
        return new ApiResponse("User profile", true, HttpStatus.OK, dto);
    }

    public ApiResponse updateMe(User currentUser, UserUpdateDto dto) {
        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            currentUser.setFullName(dto.getFullName().trim());
        }

        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().trim().isEmpty()) {
            currentUser.setPhoneNumber(dto.getPhoneNumber().trim());
        }

        if (dto.getAge() != null) {
            currentUser.setAge(dto.getAge());
        }

        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            boolean emailExists = userRepository.existsByEmail(dto.getEmail().trim());

            if (emailExists && !currentUser.getEmail().equals(dto.getEmail().trim())) {
                return new ApiResponse("Email already exists", false, HttpStatus.CONFLICT, null);
            }

            currentUser.setEmail(dto.getEmail().trim());
        }

        User savedUser = userRepository.save(currentUser);

        return new ApiResponse("Profile updated", true, HttpStatus.OK, mapToDto(savedUser));
    }

    public ApiResponse changePassword(User currentUser, ChangePasswordDto dto) {
        if (!passwordEncoder.matches(dto.getOldPassword(), currentUser.getPassword())) {
            return new ApiResponse("Old password is incorrect", false, HttpStatus.BAD_REQUEST, null);
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return new ApiResponse("New password and confirm password do not match", false, HttpStatus.BAD_REQUEST, null);
        }

        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(currentUser);

        return new ApiResponse("Password changed successfully", true, HttpStatus.OK, null);
    }

    private UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .age(user.getAge())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}