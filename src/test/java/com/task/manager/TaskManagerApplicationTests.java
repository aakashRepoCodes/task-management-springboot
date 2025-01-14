package com.task.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.manager.model.Task;
import com.task.manager.model.User;
import com.task.manager.repository.UserRepository;
import com.task.manager.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskManagerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;


    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0")
            .withDatabaseName("taskmanager")
            .withUsername("root")
            .withPassword("root");
    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureMySQLContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }


    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @BeforeEach
    void init() {
        User user = new User();
        user.setUsername("root");
        user.setPassword("{noop}root");
        user.setEmail("root@root.com");
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            userService.registerUser(user);
        }
    }


    @Test
    void register() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.com");

        mockMvc.perform(
                post("/api/user/register")
                        .contentType("application/json")
                        .content(asJsonString(user))
                        .accept("application/json")
        ).andExpect(status().isOk());
    }


    @Test
    void addTask() throws Exception {
        init();

        Task task = new Task();
        task.setTitle("title");
        task.setDescription("description");

        mockMvc.perform(
                post("/api/task")
                        .contentType("application/json")
                        .content(asJsonString(task))
                        .accept("application/json")
                        .header("Authorization", "Basic " + encodeCredentials("root", "{noop}root"))  // Add Basic Auth header
        ).andExpect(status().isCreated());
    }



    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    public static String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private String encodeCredentials(String username, String password) {
        String credentials = username + ":" + password;
        return new String(Base64.getEncoder().encode(credentials.getBytes()));
    }
}
