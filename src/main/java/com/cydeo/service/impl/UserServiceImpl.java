package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        //findAll(Sort.by("firstName")).stream().map(userMapper::convertToDTO).collect(Collectors.toList());  this one was old one
        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        //findByUserName(username)  this one was the old one
        return userMapper.convertToDTO(userRepository.findByUserNameAndIsDeleted(username,false));
    }

    @Override
    public void save(UserDTO user) {
          userRepository.save(userMapper.convertToEntity(user));


    }

    @Override
    public void deleteByUserName(String username) {

        userRepository.deleteByUserName(username);

    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        //findByUserName(userDTO.getUserName()); this one was the old one

       User user1 =  userRepository.findByUserNameAndIsDeleted(userDTO.getUserName(), false);
       User convertedUser = userMapper.convertToEntity(userDTO);
       convertedUser.setId(user1.getId());
       userRepository.save(convertedUser);


        return findByUserName(userDTO.getUserName());

    }

    @Override
    public void delete(String username) {
        // go to db and get user from db based on username
        User user1 = userRepository.findByUserNameAndIsDeleted(username,false);

        if (checkIfUserCanBeDeleted(user1)) { // if its true delete the user, if its not do not do anything. True means deletable or not.
            // change the isDeleted to true
            user1.setIsDeleted(true);
            user1.setUserName( user1.getUserName()+"-"+user1.getId() ); // if i delete user, on the table its gonna show username-id. with this way i can create same user in the database
            // and save the object db again to avoid deleting item from db, just delete from ui.
            userRepository.save(user1);
        }

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role,false);


        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }


    private boolean checkIfUserCanBeDeleted(User user){ // bu methodu baska bir classta kullanmamiza gerek yok. Neden entity kullandik ? private oldugu icin DTO da koyabiliriz Entityde

        switch (user.getRole().getDescription()){

            case "Manager":

                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDTO(user));
                return projectDTOList.size()==0; // if its 0, its gonna return true, if not its gonna return false

            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDTO(user));
                return taskDTOList.size()==0;

            default:
                return true;
        }

    }


}
