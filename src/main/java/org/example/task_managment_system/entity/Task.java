package org.example.task_managment_system.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.task_managment_system.entity.abs.AbsEntity;
import org.example.task_managment_system.entity.enums.TaskPriority;
import org.example.task_managment_system.entity.enums.TaskStatus;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Task extends AbsEntity {

    @Column(nullable = false,length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 50)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private TaskPriority taskPriority;


    private LocalDate dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedTo;









}
