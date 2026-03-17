package org.example.task_managment_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.example.task_managment_system.payload.ApiResponse;
import org.example.task_managment_system.payload.AuthLogin;
import org.example.task_managment_system.payload.AuthRegister;
import org.example.task_managment_system.service.AuthService;
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
    @Operation(summary = "Userlar ruyhatdan utish uchun api ")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRegister authRegister){
        ApiResponse apiResponse = authService.registerUser(authRegister);
        return ResponseEntity.ok(apiResponse);
    }



    @PostMapping("/login")
    @Operation(summary = "Ruyhatdan utgan user yoki admin tokwen olish uchun api")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthLogin authLogin){
        ApiResponse apiResponse = authService.login(authLogin);
        return ResponseEntity.ok(apiResponse);
    }
}
