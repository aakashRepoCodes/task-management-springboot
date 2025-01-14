package com.task.manager.service;

import com.task.manager.model.User;
import com.task.manager.model.auth.Role;
import com.task.manager.repository.RoleRepository;
import com.task.manager.repository.UserRepository;
import com.task.manager.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testRegisterUserSuccess() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@gmail.com");

        Role role = new Role(1L, "USER");

        Mockito.when(roleRepository.findByRole("ROLE_USER")).thenReturn(Optional.of(role));
        Mockito.when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        ApiResponse<String> response = userService.registerUser(user);
        assertEquals(response.getMessage() ,"ok");
        assertEquals("success", response.getStatus());

    }

    @Test
    void testRegisterUserEmailExists() {
        // Given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@gmail.com");

        Role role = new Role(1L, "USER");

        Mockito.when(roleRepository.findByRole("ROLE_USER")).thenReturn(Optional.of(role));

        Mockito.when(userRepository.existsUserByEmail(user.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
             userService.registerUser(user);
        });

        assertEquals("User already exists", exception.getMessage());

        Mockito.verify(userRepository, never()).save(user);
        Mockito.verify(passwordEncoder, never()).encode(user.getPassword());
    }
}
