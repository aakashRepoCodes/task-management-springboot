package com.task.manager.controller;

import com.task.manager.model.Status;
import com.task.manager.model.Task;
import com.task.manager.model.TaskAssignmentRequest;
import com.task.manager.service.TaskService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.saveTask(task), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(task), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTask(@RequestBody Task task) {
        taskService.deleteTask(task.getId());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign-task")
    public ResponseEntity<String> assignTask(@RequestBody TaskAssignmentRequest assignmentRequest) {
        try {
            return new ResponseEntity<>(taskService.assignTaskToUser(assignmentRequest), HttpStatus.OK);
        } catch (MessagingException e) {
            //throw new RuntimeException(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    e.getLocalizedMessage()
            );
        }

    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<List<Task>> filterTaskByStatus(@RequestParam String status) {
        return new ResponseEntity<>(
                taskService.getTaskByStatus(Status.valueOf(status.toUpperCase()))
                , HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-tasks")
    public ResponseEntity<List<Task>> getAllAssignedTaskForUser(@RequestParam String username) {
        return new ResponseEntity<>(
                taskService.getTaskForUser(username)
                ,HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unassigned-tasks")
    public  ResponseEntity<List<Task>> getAllUnAssignedTasks() {
        return new ResponseEntity<>(
                taskService.getAllUnassignedTasks(),
                HttpStatus.OK
        );
    }

}
