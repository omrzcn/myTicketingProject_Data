package com.cydeo.service;

import com.cydeo.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> findAllTasks();
    TaskDTO findById(Long id);
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);
}
