package org.example.task_managment_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.example.task_managment_system.entity.User;
import org.example.task_managment_system.payload.*;
import org.example.task_managment_system.security.CurrentUser;
import org.example.task_managment_system.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMe(@CurrentUser User currentUser) {
        ApiResponse response = authService.getMe(currentUser);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse> updateMe(@CurrentUser User currentUser,
                                                @RequestBody UserUpdateDto dto) {
        ApiResponse response = authService.updateMe(currentUser, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@CurrentUser User currentUser,
                                                      @RequestBody ChangePasswordDto dto) {
        ApiResponse response = authService.changePassword(currentUser, dto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
