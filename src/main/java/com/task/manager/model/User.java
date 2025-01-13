package com.task.manager.model;

import com.task.manager.model.auth.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @NotNull(message = "Username cannot be null")
    String username;

    @NotNull(message = "Password cannot be null")
    String password;

    @NotNull(message = "email has to be provided")
    @Column(unique = true)
    String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

}
