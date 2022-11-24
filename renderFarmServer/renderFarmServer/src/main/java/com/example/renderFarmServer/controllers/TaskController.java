package com.example.renderFarmServer.controllers;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.model.UserTask;
import com.example.renderFarmServer.service.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    RenderService taskService;

    @PostMapping("/tasks")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody UserTask userTask) {
        return taskService.taskResponse(userTask);
    }
}
