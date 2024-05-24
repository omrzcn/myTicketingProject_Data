package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {


    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {

        Project project = projectRepository.findByProjectCode(code);

        return projectMapper.convertToDTO(project);
    }

    @Override
    public List<ProjectDTO> findAllProjects() {

        List<Project> projects = projectRepository.findAll();

        return projects.stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {


        Project convertedProject = projectMapper.convertToEntity(dto);
        convertedProject.setProjectStatus(Status.OPEN); // bunu buraya koyduk, tabloda yok ama listte var
        projectRepository.save(convertedProject);  //

    }

    @Override
    public void update(ProjectDTO dto) {

        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedProject = projectMapper.convertToEntity(dto);

        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        projectRepository.save(convertedProject);




    }

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);

        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode()+"-"+project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDTO(project)); // bunu bu delete fonksiyonuna Projeler silindiginde Tasklerde silinsin diye ekledik.


    }

    @Override
    public void complete(String code) {

        Project project = projectRepository.findByProjectCode(code);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDTO(project)); // i created completeByProject method if i click complete button of project, every task related with that project need to be completed

    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {

        User user1 = userRepository.findByUserNameAndIsDeleted("harold@manager.com",false);
        List<Project> projects = projectRepository.findProjectByAssignedManager(user1);




       return projects.stream().map(project -> {

            ProjectDTO projectDTOs = projectMapper.convertToDTO(project);
            projectDTOs.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            projectDTOs.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

            return projectDTOs;

        } ).collect(Collectors.toList());






    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {

        List<Project> projectList = projectRepository.findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE,userMapper.convertToEntity(assignedManager));

        return projectList.stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }
}

/*
   UserDTO currentUserDto = userService.findByUserName("samantha@manager.com"); //when wi learn security. this will come with Security. we will say to security, give me the project who loggin the system as a manager
        User user = userMapper.convertToEntity(currentUserDto);
        List<Project> projects = projectRepository.findProjectByAssignedManager(user);

         // we have no two fields on Project that ProjectDto has which is completeTaskCounts and unfinishedTaskCounts
        return projects.stream().map(project -> {
            ProjectDTO obj = projectMapper.convertToDTO(project);
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());

 */
