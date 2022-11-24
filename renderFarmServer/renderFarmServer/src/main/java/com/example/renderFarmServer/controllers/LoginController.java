package com.example.renderFarmServer.controllers;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.service.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    RenderService signInService;
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> logIn(@RequestBody User user) {
        return signInService.logInResponse(user);
    }
}
