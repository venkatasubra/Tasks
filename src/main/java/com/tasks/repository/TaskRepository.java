package com.tasks.repository;

import com.tasks.entity.Task;
import com.tasks.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedUserId(Long userId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByDueDateBeforeAndStatus(LocalDate date, TaskStatus status);
    List<Task> findByDueDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, TaskStatus status);
}
