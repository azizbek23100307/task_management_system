package org.example.task_managment_system.repository;

import org.example.task_managment_system.entity.Task;
import org.example.task_managment_system.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @EntityGraph(attributePaths = "assignedTo")
    @Query("select t from Task t order by t.createdAt desc")
    Page<Task> findAllWithAssignedTo(Pageable pageable);

    @EntityGraph(attributePaths = "assignedTo")
    @Query("select t from Task t where t.id = :taskId")
    Optional<Task> findByIdWithAssignedTo(Integer taskId);

    @EntityGraph(attributePaths = "assignedTo")
    @Query("""
            select t
            from Task t
            where t.createdBy = :userId
               or t.assignedTo.id = :userId
            order by t.createdAt desc
            """)
    Page<Task> findAccessibleTasks(Integer userId, Pageable pageable);

    @Query("select count(t) from Task t")
    long countAllTasks();

    @Query("select count(t) from Task t where t.status = :status")
    long countAllByStatus(TaskStatus status);

    @Query("select count(t) from Task t where t.dueDate < :today and t.status <> org.example.task_managment_system.entity.enums.TaskStatus.DONE")
    long countAllOverdue(LocalDate today);

    @Query("""
            select count(t)
            from Task t
            where t.createdBy = :userId
               or t.assignedTo.id = :userId
            """)
    long countAccessibleTasks(Integer userId);

    @Query("""
            select count(t)
            from Task t
            where (t.createdBy = :userId or t.assignedTo.id = :userId)
              and t.status = :status
            """)
    long countAccessibleTasksByStatus(Integer userId, TaskStatus status);

    @Query("""
            select count(t)
            from Task t
            where (t.createdBy = :userId or t.assignedTo.id = :userId)
              and t.dueDate < :today
              and t.status <> org.example.task_managment_system.entity.enums.TaskStatus.DONE
            """)
    long countAccessibleOverdueTasks(Integer userId, LocalDate today);
}
