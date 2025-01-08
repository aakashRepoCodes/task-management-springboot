package com.task.manager.controller;

import com.task.manager.model.User;
import com.task.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class RoleController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/assign-admin")
    public String assignAdminRole(@RequestBody User user) {
       return userService.assignAdminRole(user);
    }
}
