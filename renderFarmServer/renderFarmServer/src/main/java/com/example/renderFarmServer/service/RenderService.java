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

    @Value("${messages.signInSuccessful}")
    private String signInSuccessful;

    @Value("${messages.incorrectPassword}")
    private String incorrectPassword;

    @Value("${messages.incorrectID}")
    private String incorrectID;

    @Value("${messages.taskSaved}")
    private String taskSaved;

    public ResponseEntity<String> signUpResponse(User user) {
        if(!userRepository.existsById(user.getUsername())) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(signUpSuccessful);
        } else return ResponseEntity.status(400).body(signUpFailed);
    }

    public ResponseEntity<String> logInResponse(User user) {
            if(userRepository.existsById(user.getUsername())) {
            if(userRepository.userAndPasswordExist(user.getUsername(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(signInSuccessful);
            } else return ResponseEntity.status(400).body(incorrectPassword);
        }
        return ResponseEntity.status(400).body(incorrectID);
    }

    public ResponseEntity<String> taskResponse(UserTask task) {
        userTasksRepository.save(task);
        return ResponseEntity.status(400).body(taskSaved);
    }


}
