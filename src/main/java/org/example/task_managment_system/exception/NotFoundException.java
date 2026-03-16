package org.example.task_managment_system.exception;


import org.example.task_managment_system.payload.ApiResponse;

public class NotFoundException extends RuntimeException{
    public NotFoundException (ApiResponse message){
        super(message.toString());
    }
}
