package com.example.renderFarmServer.service;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.model.UserTask;
import com.example.renderFarmServer.model.UserTaskStatus;
import com.example.renderFarmServer.persistence.UserRepository;
import com.example.renderFarmServer.persistence.UserTasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class RenderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTasksRepository userTasksRepository;

    private class ScheduledTaskCompletion extends TimerTask {

        public ScheduledTaskCompletion(){}

        public ScheduledTaskCompletion(UserTask userTask) {
            this.userTask = userTask;
        }

        private UserTask userTask;

        public void run() {
            userTask.setEnd_time(userTask.getStart_time().plusSeconds(userTask.getRender_time()));
            userTask.setStatus(UserTaskStatus.COMPLETE);
            userTasksRepository.updateTask(userTask.getEnd_time(), userTask.getStatus().toString(), userTask.getTask_id().toString());
        }

    }

    @Value("${messages.signUpSuccessful}")
    private String signUpSuccessful;

    @Value("${messages.signUpFailed}")
    private String signUpFailed;

    @Value("${messages.taskCreated}")
    private String taskCreated;

    @Value("${messages.nonExistentUser}")
    private String nonExistentUser;

    @Value("${messages.error}")
    private String error;

    public ResponseEntity<String> signUpResponse(User user) {
        if(!userRepository.existsById(user.getUsername())) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(signUpSuccessful);
        }
        return ResponseEntity.status(400).body(signUpFailed);
    }

    public ResponseEntity<String> createNewTaskResponse(UserTask userTask) {
        if(userRepository.existsById(userTask.getUsername())) {
            UserTask newTask = new UserTask(userTask);
            userTasksRepository.save(newTask);
            new Timer().schedule(new ScheduledTaskCompletion(newTask), newTask.getRender_time() * 1000);
            return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
        }
        return ResponseEntity.status(400).body(nonExistentUser);
    }

    public ResponseEntity<HashMap<String, Object>> browseCurrentTasksResponse(String username) {
        HashMap<String, Object> outputMap = new HashMap<>();
        if(userRepository.existsById(username)) {
            outputMap.put("taskList", new ArrayList<>(userTasksRepository.tasksList(username)
                    .stream().map(UserTask::toHashMap).toList()));
            return ResponseEntity.status(HttpStatus.OK).body(outputMap);
        }
        outputMap.put(error, nonExistentUser);
        return ResponseEntity.status(400).body(outputMap);
    }

}
