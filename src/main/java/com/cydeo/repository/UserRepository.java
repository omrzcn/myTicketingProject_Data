package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserNameAndIsDeleted(String username,Boolean deleted);

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted); // this is for finding employee if we delete employee from ui. If its true , its gonna return me all deleted users, if false all the users not deleted.

    @Transactional
    void deleteByUserName(String username);

    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description,Boolean deleted);

}
