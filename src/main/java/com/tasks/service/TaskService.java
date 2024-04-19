package com.tasks.service;

import com.tasks.advice.ResourceNotFoundException;
import com.tasks.entity.Task;
import com.tasks.entity.TaskStatistics;
import com.tasks.entity.TaskStatus;
import com.tasks.entity.User;
import com.tasks.repository.TaskRepository;
import com.tasks.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task task) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        // Update existingTask with task details
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setStatus(task.getStatus());

        if (task.getStatus() == TaskStatus.COMPLETED) {
            existingTask.setCompletedDate(LocalDate.now());
        }

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        taskRepository.delete(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        task.setAssignedUser(user);
        taskRepository.save(task);
    }

    public List<Task> getOverdueTasks() {
        LocalDate currentDate = LocalDate.now();
        return taskRepository.findByDueDateBeforeAndStatus(currentDate, TaskStatus.IN_PROGRESS);
    }

    public Task setTaskProgress(Long taskId, int progress) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + taskId));

        // Validate progress value
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress value must be between 0 and 100");
        }

        // Update task progress
        // Example: task.setProgress(progress);

        return taskRepository.save(task);
    }

    public List<Task> getTasksAssignedToUser(Long userId) {
        return taskRepository.findByAssignedUserId(userId);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public List<Task> getCompletedTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findByDueDateBetweenAndStatus(startDate, endDate, TaskStatus.COMPLETED);
    }

    public TaskStatistics getTaskStatistics() {
        List<Task> allTasks = taskRepository.findAll();
        long totalTasks = allTasks.size();
        long completedTasks = allTasks.stream().filter(task -> task.getStatus() == TaskStatus.COMPLETED).count();
        double completionPercentage = (totalTasks == 0) ? 0 : ((double) completedTasks / totalTasks) * 100;

        return new TaskStatistics(totalTasks, completedTasks, completionPercentage);
    }

}
