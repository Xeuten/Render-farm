package com.example.renderFarmServer.controllers;

import com.example.renderFarmServer.service.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusHistoryController {
    @Autowired
    RenderService taskHistoryService;

    @GetMapping("/task_history/{username}/{taskName}")
    @ResponseBody
    public ResponseEntity<String> viewTaskHistory(@PathVariable String username,
                                                                      @PathVariable String taskName) {
        return taskHistoryService.viewTaskHistoryResponse(username, taskName);
    }
}
