package com.cydeo.repository;

import com.cydeo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findAllBy();
    Optional<Task> findById(Long id);
}
