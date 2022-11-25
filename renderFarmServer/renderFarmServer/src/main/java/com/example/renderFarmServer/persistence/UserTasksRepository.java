package com.example.renderFarmServer.persistence;

import com.example.renderFarmServer.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

public interface UserTasksRepository extends JpaRepository<UserTask, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_tasks SET end_time = :endTime, status = :newStatus WHERE task_id = :taskID",
            nativeQuery = true)
    void updateTask(@Param("endTime") OffsetDateTime endTime, @Param("newStatus") String newStatus,
                    @Param("taskID") String taskID);

    @Query(value = "SELECT * FROM user_tasks WHERE username = :username", nativeQuery = true)
    List<UserTask> tasksList(@Param("username") String username);
}