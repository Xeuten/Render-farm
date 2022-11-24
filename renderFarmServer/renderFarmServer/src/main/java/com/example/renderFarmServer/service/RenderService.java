package com.example.renderFarmServer.service;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.model.UserTask;
import com.example.renderFarmServer.persistence.UserRepository;
import com.example.renderFarmServer.persistence.UserTasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RenderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTasksRepository userTasksRepository;

    @Value("${messages.signUpSuccessful}")
    private String signUpSuccessful;

    @Value("${messages.signUpFailed}")
    private String signUpFailed;

    @Value("${messages.taskCreated}")
    private String taskCreated;

    @Value(("${messages.nonExistentUser}"))
    private String nonExistentUser;

    public ResponseEntity<String> signUpResponse(User user) {
        if(!userRepository.existsById(user.getUsername())) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(signUpSuccessful);
        }
        return ResponseEntity.status(400).body(signUpFailed);
    }

    public ResponseEntity<String> createNewTaskResponse(UserTask userTask) {
        if(userRepository.existsById(userTask.getUsername())) {
            userTasksRepository.save(new UserTask(userTask));
            return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
        }
        return ResponseEntity.status(400).body(nonExistentUser);
    }

}
