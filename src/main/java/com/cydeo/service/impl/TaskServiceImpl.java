package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDTO> findAllTasks() {

       List<Task> tasks = taskRepository.findAllBy();


        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        TaskDTO convertedTask = taskMapper.convertToDTO(task.get());
        return convertedTask;
    }

    @Override
    public void save(TaskDTO dto) {

        Task task1 = taskMapper.convertToEntity(dto);
        task1.setAssignedDate(LocalDate.now());
        task1.setTaskStatus(Status.OPEN);
        taskRepository.save(task1);

    }

    @Override
    public void update(TaskDTO dto) {

       Task task1= taskMapper.convertToEntity(dto);
       task1.setId(dto.getId());
       task1.setTaskStatus(dto.getTaskStatus());
       task1.setAssignedDate(LocalDate.now());
       taskRepository.save(task1);




    }

    @Override
    public void delete(Long id) {
        Task task1 = taskRepository.findById(id).get();
        task1.setIsDeleted(true);
        taskRepository.save(task1);
    }
}
