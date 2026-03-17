package org.example.task_managment_system.service;

import lombok.RequiredArgsConstructor;
import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.exception.ForbiddenOperationException;
import org.example.task_managment_system.exception.ResourceNotFoundException;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.UserResponseDto;
import org.example.task_managment_system.repository.TaskRepository;
import org.example.task_managment_system.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TaskService taskService;

    @Override
    public ApiResponse getUsers(Integer currentUserId) {

        List<UserResponseDto> users = userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                ))
                .toList();

        return ApiResponse.builder()
                .message("Userlar ro'yxati")
                .success(true)
                .status(HttpStatus.OK)
                .body(users)
                .build();
    }

    @Override
    public ApiResponse getTasks(int page, int size, Integer currentUserId) {
        getAdminOrThrow(currentUserId);
        return taskService.getTasks(page, size, currentUserId);
    }

    @Override
    @Transactional
    public ApiResponse deleteUser(Integer userId, Integer currentUserId) {
        User currentUser = getAdminOrThrow(currentUserId);

        if (currentUser.getId().equals(userId)) {
            throw new ForbiddenOperationException("Admin o'zini o'chira olmaydi");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi: " + userId));

        userRepository.delete(user);

        return ApiResponse.builder()
                .message("User muvaffaqiyatli o'chirildi")
                .success(true)
                .status(HttpStatus.OK)
                .body(null)
                .build();
    }

    private User getAdminOrThrow(Integer currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User topilmadi: " + currentUserId));

        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new ForbiddenOperationException("Bu amal faqat admin uchun");
        }

        return user;
    }
}
