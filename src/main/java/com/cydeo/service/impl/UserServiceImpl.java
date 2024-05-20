package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        return userRepository.findAll(Sort.by("firstName")).stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDTO(userRepository.findByUserName(username));
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

       User user1 =  userRepository.findByUserName(userDTO.getUserName());
       User convertedUser = userMapper.convertToEntity(userDTO);
       convertedUser.setId(user1.getId());
       userRepository.save(convertedUser);


        return findByUserName(userDTO.getUserName());

    }

    @Override
    public void delete(String username) {
        // go to db and get user from db based on username
        User user1 = userRepository.findByUserName(username);
        // change the isDeleted to true
        user1.setIsDeleted(true);
        // and save the object db again to avoid deleting item from db, just delete from ui.
        userRepository.save(user1);

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCase(role);


        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }


}
