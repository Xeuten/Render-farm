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

    // Этот запрос обновляет поля "endTime" и "status" строки таблицы с заданным id задачи.
    @Modifying
    @Transactional
    @Query(value = "UPDATE user_tasks SET end_time = :endTime, status = :newStatus WHERE task_id = :taskID",
            nativeQuery = true)
    void updateTask(@Param("endTime") OffsetDateTime endTime, @Param("newStatus") String newStatus,
                    @Param("taskID") String taskID);

    // Этот запрос получает список задач заданного пользователя.
    @Query(value = "SELECT * FROM user_tasks WHERE username = :username", nativeQuery = true)
    List<UserTask> tasksList(@Param("username") String username);

    // Этот запрос проверяет, содержится ли в таблице строка с заданными именем пользователя и именем задачи.
    @Query(value = "SELECT CASE WHEN COUNT(task_name) > 0 THEN TRUE ELSE FALSE END FROM user_tasks WHERE username = :username AND task_name = :taskName",
            nativeQuery = true)
    boolean taskExists(@Param("username") String username, @Param("taskName") String taskName);

    // Этот запрос возвращает задачу из таблицы по заданным имени пользователя и имени задачи.
    @Query(value = "SELECT * FROM user_tasks WHERE username = :username AND task_name = :taskName LIMIT 1",
            nativeQuery = true)
    UserTask selectTask(@Param("username") String username, @Param("taskName") String taskName);

}