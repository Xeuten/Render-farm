package com.example.renderFarmServer.controllers;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.service.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class CurrentTasksController {

    @Autowired
    RenderService currentTasksService;

    @GetMapping("/current_tasks/{username}")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> browseCurrentTasks(@PathVariable String username) {
        return currentTasksService.browseCurrentTasksResponse(username);
    }
}
