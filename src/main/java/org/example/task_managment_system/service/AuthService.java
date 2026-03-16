package org.example.task_managment_system.service;

import lombok.RequiredArgsConstructor;

import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.entity.enums.Role;
import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.AuthLogin;
import org.example.task_managment_system.payload.AuthRegister;
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
        boolean b = userRepository.existsByEmail(authRegister.getEmail());
        if (b) {
            return new ApiResponse("User already exists", false, HttpStatus.ALREADY_REPORTED, null);
        }

        User user = User.builder()
                .email(authRegister.getEmail())
                .fullName(authRegister.getFullName())
                .age(authRegister.getAge())
                .phoneNumber(authRegister.getPhoneNumber())
                .password(passwordEncoder.encode(authRegister.getPassword()))
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();
        userRepository.save(user);
        return new ApiResponse("User registered", true, HttpStatus.OK, null);
    }


    public ApiResponse login(AuthLogin authLogin){
        User user = userRepository.findByEmail(authLogin.getEmail()).orElse(null);
        if(user == null){
            return new ApiResponse("User not found", false, HttpStatus.NOT_FOUND, null);
        }

        if(passwordEncoder.matches(authLogin.getPassword(), user.getPassword())){
            String token = jwtProvider.generateToken(authLogin.getEmail());
            return new ApiResponse("Success", true, HttpStatus.OK, token);
        }

        return new ApiResponse("Wrong password", false, HttpStatus.BAD_REQUEST, null);
    }




}
