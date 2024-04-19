package com.tasks.controller;

import com.tasks.entity.Task;
import com.tasks.entity.TaskStatistics;
import com.tasks.entity.TaskStatus;
import com.tasks.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @Valid @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<Void> assignTaskToUser(@PathVariable Long taskId, @RequestBody Long userId) {
        taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        List<Task> overdueTasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(overdueTasks);
    }

    @PutMapping("/{taskId}/progress")
    public ResponseEntity<Task> setTaskProgress(@PathVariable Long taskId, @RequestBody int progress) {
        Task updatedTask = taskService.setTaskProgress(taskId, progress);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<Task>> getTasksAssignedToUser(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksAssignedToUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Task> completedTasks = taskService.getCompletedTasksByDateRange(startDate, endDate);
        return ResponseEntity.ok(completedTasks);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TaskStatistics> getTaskStatistics() {
        TaskStatistics statistics = taskService.getTaskStatistics();
        return ResponseEntity.ok(statistics);
    }


}
