package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
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
    private final UserService userService;
    private final UserMapper userMapper;



    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
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
        convertedTask.setAssignedDate(task1.getAssignedDate());
        convertedTask.setId(task1.getId());
        convertedTask.setTaskStatus(dto.getTaskStatus()==null ? task1.getTaskStatus() : dto.getTaskStatus()); // burda diyoruzki  eger dto status'u null sa task1 id deki task statusu ekle, eger null degilse dto'nun kendi status'unun al
       taskRepository.save(convertedTask);




    }

    @Override
    public void delete(Long id) {
        Task task1 = taskRepository.findById(id).get();
        task1.setIsDeleted(true);
        taskRepository.save(task1);
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {


        List<Task> tasks = taskRepository.findByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE,userMapper.convertToEntity(assignedEmployee));

        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }




    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com"); // Giris yapan employeemizin dto'sunu aldik hardcode ile.
        List<Task> tasks = taskRepository.findByTaskStatusIsNotAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser)); // sonra ona ait olan tasklarin complete olmayanlarini getirecek bir method yazdik taskRepositoryde.
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList()); // sonra onlari dto'ya cevirip UI'de gosterdik.

    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com"); // Giris yapan employeemizin dto'sunu aldik hardcode ile.
        List<Task> tasks = taskRepository.findByTaskStatusAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser)); // sonra ona ait olan tasklarin complete olmayanlarini getirecek bir method yazdik taskRepositoryde.
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList()); // sonra onlari dto'ya cevirip UI'de gosterdik.



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

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        Project project = projectMapper.convertToEntity(projectDTO); // dto yu entitye cevir cunku DB ile calisacaz
        List<Task> tasks = taskRepository.findAllByProject(project); // bu projeye ait tum tasklari findAllByProject methoduyla bul

        tasks.stream().map(taskMapper::convertToDTO).forEach(taskDTO -> {
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);
        }); // sonra bu task'lari dto ya cevir , statuslerini complete yap ve en son update methodunu dondur.


            }
}
