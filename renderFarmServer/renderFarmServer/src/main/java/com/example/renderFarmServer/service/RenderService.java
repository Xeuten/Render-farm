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

import java.util.*;

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

        //Этот внутренний класс нужен для назначения отложенной смены статуса задачи при её создании.
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

    @Value("${messages.signInSuccessful}")
    private String signInSuccessful;

    @Value("${messages.signInFailed}")
    private String signInFailed;

    @Value("${messages.taskAlreadyExists}")
    private String taskAlreadyExists;

    @Value("${messages.taskCreated}")
    private String taskCreated;

    @Value("${messages.nonExistentUser}")
    private String nonExistentUser;

    @Value("${messages.taskList}")
    private String taskList;

    @Value("${messages.error}")
    private String error;

    @Value("${messages.browseFailed}")
    private String browseFailed;

    @Value("${messages.nonExistentTask}")
    private String nonExistentTask;

    @Value("${messages.viewFailed}")
    private String viewFailed;


    public ResponseEntity<String> signUpResponse(User user) {
        if(!userRepository.existsById(user.getUsername())) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(signUpSuccessful);
        }
        return ResponseEntity.status(400).body(signUpFailed);
    }

    public ResponseEntity<String> signInResponse(String username) {
        if(userRepository.existsById(username)) return ResponseEntity.status(HttpStatus.OK).body(signInSuccessful);
        return ResponseEntity.status(404).body(signInFailed);
    }


    public ResponseEntity<String> createNewTaskResponse(UserTask userTask) {
        if(userRepository.existsById(userTask.getUsername())) {
            if(userTasksRepository.taskExists(userTask.getUsername(), userTask.getTask_name()))
                return ResponseEntity.status(400).body(taskAlreadyExists);
            UserTask newTask = new UserTask(userTask);
            userTasksRepository.save(newTask);
            new Timer().schedule(new ScheduledTaskCompletion(newTask), newTask.getRender_time() * 1000);
            return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
        }
        return ResponseEntity.status(404).body(nonExistentUser);
    }

    public ResponseEntity<HashMap<String, Object>> browseCurrentTasksResponse(String username) {
        HashMap<String, Object> outputMap = new HashMap<>();
        if(userRepository.existsById(username)) {
            outputMap.put(taskList, new ArrayList<>(userTasksRepository.tasksList(username)
                    .stream().map(UserTask::toHashMap).toList()));
            return ResponseEntity.status(HttpStatus.OK).body(outputMap);
        }
        outputMap.put(error, browseFailed);
        return ResponseEntity.status(404).body(outputMap);
    }

    public ResponseEntity<String> viewTaskHistoryResponse(String username, String taskName) {
        if(userRepository.existsById(username)) {
            if(!userTasksRepository.taskExists(username, taskName)) return ResponseEntity.status(404).body(nonExistentTask);
            UserTask selectedTask = userTasksRepository.selectTask(username, taskName);
            String outputStr = "RENDERING: " + selectedTask.getStart_time().toString();
            if(selectedTask.getEnd_time() != null) outputStr += "\nCOMPLETE: " + selectedTask.getEnd_time().toString();
            return ResponseEntity.status(HttpStatus.OK).body(outputStr);
        }
        return ResponseEntity.status(404).body(viewFailed);
    }

}
