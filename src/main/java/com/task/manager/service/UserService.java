package com.task.manager.service;

import com.task.manager.model.User;
import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import com.task.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
        Role role = Optional.of(roleRepository.findByRole("ROLE_USER")).orElseThrow( () ->
                new RuntimeException("Role Not Found"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        return "Register successful";
    }

    public String login(User user) {

       Authentication authentication =  authenticationManager.authenticate(
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

}
