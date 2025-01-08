package com.task.manager.service;

import com.task.manager.model.Status;
import com.task.manager.model.Task;
import com.task.manager.model.User;
import com.task.manager.model.auth.UserPrincipal;
import com.task.manager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        if (getCurrentUser().getRole().getRole().contains("ADMIN")) {
            return taskRepository.findAll();
        } else {
            Long userId = getCurrentUser().getId();
            return taskRepository.findAllByUserId(userId);
        }
    }

    public Task getTaskById(Long id) {
        return Optional.of(taskRepository.findById(id).get()).orElseThrow(
                () -> new RuntimeException("Task not found")
        );
    }

    public List<Task> getTaskByStatus(Status status) {
       return taskRepository.findAllByStatus(status);
    }

    public Task saveTask(Task task) {
        task.setUser(getCurrentUser());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Task not found")
        );
        if (task != null) {
            taskRepository.delete(task);
        }
    }

    public Task updateTask(Task task) {
       Optional<Task> existingTask  =  taskRepository.findById(task.getId());
       if(existingTask.isPresent()) {
           Task newTask = existingTask.get();
           newTask.setDescription(task.getDescription());
           newTask.setTitle(task.getTitle());
           newTask.setDueDate(task.getDueDate());
           newTask.setPriority(task.getPriority());
           newTask.setStatus(task.getStatus());
           return taskRepository.save(newTask);
       } else {
           throw new RuntimeException("Task not found");
       }
    }


    private UserPrincipal getPrincipalUser() {
        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    private User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUser();
    }
}
