package com.task.manager.controller;

import com.task.manager.model.Task;
import com.task.manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.saveTask(task);

    }

    @PostMapping("/update")
    public Task updateTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }

  /*  @PostMapping
    public String assignTask(@RequestBody Task task) {


    }*/

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

   /* @GetMapping
    public List<Task> getTasks(@RequestParam Status status) {


    }*/


}
