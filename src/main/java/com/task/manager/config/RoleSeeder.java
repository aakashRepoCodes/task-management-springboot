package com.task.manager.config;

import com.task.manager.model.Priority;
import com.task.manager.model.Status;
import com.task.manager.model.Task;
import com.task.manager.model.User;
import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import com.task.manager.repository.TaskRepository;
import com.task.manager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class RoleSeeder implements CommandLineRunner {

    @Autowired
    TaskRepository taskRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RoleSeeder(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByRole("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_USER"));
            log.info("Role added {} : ", "ROLE_USER");
        }
        if (roleRepository.findByRole("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
            log.info("Role added {} : ", "ROLE_ADMIN");
        }
        if (roleRepository.findByRole("ROLE_SUPER_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_SUPER_ADMIN"));
            log.info("Role added {} : ", "ROLE_SUPER_ADMIN");
        }
       /* for (int i = 0; i < 200; i++) {
            taskRepository.save(
                    new Task(
                            null,
                            "Title " + i,
                            "Description for task " + i,
                            LocalDate.of(2024, 2, 28).toString(),       // LocalDate for the due date
                            Priority.Medium,
                            Status.COMPLETED,
                            new User()
                    )
            );
        }*/

        // Check if a SUPER_ADMIN user exists
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("AdminRandomAppPassword"));
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setRole(roleRepository.findByRole("ROLE_SUPER_ADMIN").get());
            userRepository.save(superAdmin);
            log.info("Superadmin added {} : ", "superadmin@example.com");
        }
    }
}
