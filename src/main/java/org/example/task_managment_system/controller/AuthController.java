package com.example.azizaka.controller;

import lombok.RequiredArgsConstructor;
import com.example.azizaka.payload.ApiResponse;
import com.example.azizaka.payload.AuthLogin;
import com.example.azizaka.payload.AuthRegister;
import com.example.azizaka.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRegister authRegister){
        ApiResponse apiResponse = authService.registerUser(authRegister);
        return ResponseEntity.ok(apiResponse);
    }



    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthLogin authLogin){
        ApiResponse apiResponse = authService.login(authLogin);
        return ResponseEntity.ok(apiResponse);
    }
}
