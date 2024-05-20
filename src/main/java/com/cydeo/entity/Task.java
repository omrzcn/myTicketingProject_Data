package com.cydeo.entity;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted_false")
public class Task extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id") // this is foreign key
    private User assignedEmployee;

    @NotBlank
    private String taskSubject;

    @NotBlank
    private String taskDetail;
    @Enumerated(EnumType.STRING)
    private Status taskStatus;

    @Column(columnDefinition = "DATE") // it works without this.
    private LocalDate assignedDate;



}
