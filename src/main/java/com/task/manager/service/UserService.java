package com.task.manager.service;

import com.task.manager.model.User;
import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import com.task.manager.repository.UserRepository;
import com.task.manager.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class UserService {

    final
    AuthenticationManager authenticationManager;

    final
    RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<String> registerUser(User user) {
        Role role = roleRepository.findByRole("ROLE_USER").orElseThrow(() ->
                new RuntimeException("Role Not Found"));

        Optional<User> existsUser = userRepository.existsUserByEmail(user.getEmail());
        if (existsUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        ApiResponse<String> response = new ApiResponse<String>(
             "success", "ok", "Registered Successfully"
        );
        return response;
    }

    public String login(User user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword())
        );
        if (authentication.isAuthenticated()) {
            ApiResponse<String> response = new ApiResponse<String>(
                    "success", "Logged IN Success", null
            );
            return "Login successful";
        } else {
            throw new RuntimeException("Incorrect username or password");
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
