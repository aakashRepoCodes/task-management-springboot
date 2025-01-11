package com.task.manager.service;

import com.task.manager.exception.TaskNotFoundException;
import com.task.manager.exception.UserNotFoundException;
import com.task.manager.model.Status;
import com.task.manager.model.Task;
import com.task.manager.model.TaskAssignmentRequest;
import com.task.manager.model.User;
import com.task.manager.model.auth.UserPrincipal;
import com.task.manager.repository.TaskRepository;
import com.task.manager.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

  /*  public List<Task> getAllTasks(Pageable pageable) {
        if (getCurrentUser().getRole().getRole().contains("ADMIN")) {
            return taskRepository.findAll();
        } else {
            Long userId = getCurrentUser().getId();
            return taskRepository.findAllByUserId(userId);
        }
    }*/

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }


    public List<Task> getTaskForUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        return taskRepository.findAllByUserId(user.getId());
    }

    public List<Task> getTaskByStatus(Status status) {
       return taskRepository.findAllByStatus(status);
    }

    public Task saveTask(Task task) {
        task.setUser(getCurrentUser());
        Task newTask =  taskRepository.save(task);
        log.info("New Task {} created {}", newTask.getId(), newTask.getTitle());
        return newTask;

    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task not found")
        );
        if (task != null) {
            taskRepository.delete(task);
            log.info("Task Deleted {}, title {}", task.getId(), task.getTitle());
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
           log.info("Task Updated {}, title {}", task.getId(), task.getTitle());
           return taskRepository.save(newTask);
       } else {
           throw new TaskNotFoundException("Task not found");
       }
    }

    public String assignTaskToUser(TaskAssignmentRequest assignmentRequest) throws MessagingException {
        Task existingTask = taskRepository.findById(assignmentRequest.getTaskId()).orElseThrow(
                () -> new TaskNotFoundException("Task not found")
        );

        User user = userRepository.findByUsername(assignmentRequest.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        if (existingTask.getUser().getUsername() != null && existingTask.getUser().getUsername().isEmpty()) {

            if (existingTask.getUser().getUsername().equals(assignmentRequest.getUsername())) {
                // task is already assigned to same user
                log.info("Task {} was already assigned to user {}", existingTask.getId(), user.getUsername());
                return "Task was already assigned to user : " + user.getUsername() +
                        "previously.";
            }
        } else {
           // update the task assignment
            existingTask.setUser(user);
            taskRepository.save(existingTask);
            log.info("User {} is notified with email {} about task {} ", user.getUsername()
                    , user.getEmail(), existingTask.getId());
            emailService.sendTaskAssignedEmail(user.getEmail(), existingTask);
        }
        return "Task is now assigned to user : " + user.getUsername();
    }

    public List<Task> getAllUnassignedTasks() {
        List<Task> tasks = taskRepository.findAllUnassignedTasks();
        return tasks;
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
