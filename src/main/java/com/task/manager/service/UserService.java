package com.task.manager.service;

import com.task.manager.model.User;
import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import com.task.manager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {
        Role role = roleRepository.findByRole("ROLE_USER").orElseThrow(() ->
                new RuntimeException("Role Not Found"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        return "Register successful";
    }

    public String login(User user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return "Login successful";
        } else {
            return "Login failed";
        }
    }

    public String assignAdminRole(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Role adminRole = roleRepository.findByRole("ROLE_ADMIN").orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        existingUser.setRole(adminRole);
        userRepository.save(existingUser);

        log.info(" {} is now admin", user.getUsername());
        return existingUser.getUsername() + "User assigned as admin";
    }
}
