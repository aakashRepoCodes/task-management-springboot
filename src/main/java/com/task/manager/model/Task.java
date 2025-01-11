package com.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     String dueDate;
     Priority priority ;
     Status status;

     @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
     @JsonIgnore
     User user;
}
