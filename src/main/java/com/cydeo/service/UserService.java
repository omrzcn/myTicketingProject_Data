package com.cydeo.service;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;

import java.util.List;

public interface UserService {

    List<UserDTO> findAllUsers();
    UserDTO findByUserName(String username);

    void save(UserDTO user);

    void deleteByUserName(String username);

    UserDTO update (UserDTO userDTO);

    void delete(String username);

    List<UserDTO>listAllByRole(String role); // we'll use this method next buildings.

}
