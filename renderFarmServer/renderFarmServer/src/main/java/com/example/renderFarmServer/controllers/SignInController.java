package com.example.renderFarmServer.controllers;

import com.example.renderFarmServer.service.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignInController {

    @Autowired
    RenderService signInService;

    @GetMapping("/sign_in/{username}")
    @ResponseBody
    public ResponseEntity<String> signIn(@PathVariable String username) {
        return signInService.signInResponse(username);
    }
}
