package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> findAllProjects();

    void save (ProjectDTO dto);
    void update(ProjectDTO dto);
    void delete (String code);
    void complete (String code);

    List<ProjectDTO> listAllProjectDetails();


}
