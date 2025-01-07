package com.task.manager.service;

import com.task.manager.model.User;
import com.task.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        userRepository.save(user);
        return "Register successful";
    }

}
