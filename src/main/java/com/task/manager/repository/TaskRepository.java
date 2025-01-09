package com.task.manager.repository;

import com.task.manager.model.Status;
import com.task.manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByStatus(Status status);

    List<Task> findAllByUserId(Long userId);

    @Query("SELECT t from Task t where t.user IS NULL ")
    List<Task> findAllUnassignedTasks();

}
