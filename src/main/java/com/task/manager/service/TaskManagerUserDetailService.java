package com.task.manager.service;

import com.task.manager.model.User;
import com.task.manager.model.auth.UserPrincipal;
import com.task.manager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TaskManagerUserDetailService  implements UserDetailsService {

    private final UserRepository userRepository;

    public TaskManagerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        if (user != null) {
            return new UserPrincipal(user);
        }

        return null;
    }
}
