package com.example.azizaka.exception;


import com.example.azizaka.payload.ApiResponse;

public class NotFoundException extends RuntimeException{
    public NotFoundException (ApiResponse message){
        super(message.toString());
    }
}
