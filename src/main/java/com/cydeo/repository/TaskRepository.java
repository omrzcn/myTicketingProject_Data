package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findAllBy();
    Optional<Task> findById(Long id);
    List<Task> findByTaskStatus(Status status);
    List<Task>findByTaskStatusIsNotAndAssignedEmployee (Status status, User user);
    List<Task> findByTaskStatusAndAssignedEmployee (Status status,User user);

    @Query(value = "select count(t) from Task t where t.project.projectCode=?1 and t.taskStatus <> 'COMPLETE'" )
    int totalNonCompletedTask(String projectCode);

    @Query(value = "select count (*) from tasks t join projects p on t.project_id = p.id where p.project_code = ?1 and t.task_status='COMPLETE'",nativeQuery = true)
    int totalCompletedTask(String projectCode);

    List<Task> findAllByProject(Project project); // bunu projenin tasklarini bulmak icin ekledik
}
