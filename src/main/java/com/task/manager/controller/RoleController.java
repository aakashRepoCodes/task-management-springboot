package com.task.manager.controller;

import com.task.manager.model.User;
import com.task.manager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class RoleController {

    final UserService userService;

    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/assign-admin")
    public ResponseEntity<String> assignAdminRole(@RequestBody User user) {
       return new ResponseEntity<>(
               userService.assignAdminRole(user),
               HttpStatus.OK
       );
    }
}
