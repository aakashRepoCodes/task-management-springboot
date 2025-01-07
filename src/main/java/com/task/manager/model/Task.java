package com.task.manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}