package com.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String title;
    String description;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    Priority priority;
    Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    User user;
}
