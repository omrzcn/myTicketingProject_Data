package com.cydeo.entity;

import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "projects")
@NoArgsConstructor
@Where(clause = "is_deleted = false")
public class Project extends BaseEntity {


    private String projectName;


    private String projectCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(columnDefinition = "DATE")
    private LocalDate startDate;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(columnDefinition = "DATE")
    private LocalDate endDate;


    private String projectDetail;
    @Enumerated(EnumType.STRING)
    private Status projectStatus;




    public Project(String projectName, String projectCode, User assignedManager, LocalDate startDate, LocalDate endDate, String projectDetail, Status projectStatus) {
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.assignedManager = assignedManager;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectDetail = projectDetail;
        this.projectStatus = projectStatus;
    }
}
