package com.task.manager.config;

import com.task.manager.model.User;
import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import com.task.manager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

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
            System.out.println("Default role ROLE_USER added to the database.");
        }
        if (roleRepository.findByRole("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
            System.out.println("Admin role ROLE_ADMIN added to the database.");
        }
        if (roleRepository.findByRole("ROLE_SUPER_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_SUPER_ADMIN"));
            System.out.println("Super Admin role ROLE_SUPER_ADMIN added to the database.");
        }

        // Check if a SUPER_ADMIN user exists
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("AdminRandomAppPassword"));
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setRole(roleRepository.findByRole("ROLE_SUPER_ADMIN").get());
            userRepository.save(superAdmin);
            System.out.println("SUPER_ADMIN user created with username: superadmin.");
        }
    }
}
