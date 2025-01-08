package com.task.manager.config;

import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByRole("ROLE_USER") == null) {
            roleRepository.save(new Role(null, "ROLE_USER"));
            System.out.println("Default role ROLE_USER added to the database.");
        }
        if (roleRepository.findByRole("ROLE_ADMIN") == null) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
            System.out.println("Admin role ROLE_ADMIN added to the database.");
        }
    }
}
