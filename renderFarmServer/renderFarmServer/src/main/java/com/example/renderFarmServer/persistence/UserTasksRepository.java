package com.example.renderFarmServer.persistence;

import com.example.renderFarmServer.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTasksRepository extends JpaRepository<UserTask, Long> {
}
