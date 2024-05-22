package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.ProjectRepository;
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
    private final ProjectMapper projectMapper;



    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
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
        Task task1 = taskRepository.findById(dto.getId()).get();
       Task convertedTask= taskMapper.convertToEntity(dto);
        convertedTask.setAssignedDate(LocalDate.now());
        convertedTask.setId(task1.getId());
        convertedTask.setTaskStatus(task1.getTaskStatus());
       taskRepository.save(convertedTask);




    }

    @Override
    public void delete(Long id) {
        Task task1 = taskRepository.findById(id).get();
        task1.setIsDeleted(true);
        taskRepository.save(task1);
    }



    @Override
    public List<TaskDTO> findAllTasksByStatusIsNot(Status status) {

        return null;
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {


        return taskRepository.totalNonCompletedTask(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTask(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project project = projectMapper.convertToEntity(projectDTO); // projeyi entity ye cevirdik. Cunku DB den bu projenin Tasklerini bulacaz
        List<Task> tasks = taskRepository.findAllByProject(project); // bu projenin tasklerini bulduk
       tasks.forEach(task -> delete(task.getId())); // teker teker sildik

    }
}
