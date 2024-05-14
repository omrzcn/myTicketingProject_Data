package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

   private  ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public  RoleDTO convertToDTO(Role role){

       return modelMapper.map(role,RoleDTO.class);

    }
    public Role convertToEntity(RoleDTO role){

        return modelMapper.map(role,Role.class);
    }

}
