package com.example.renderFarmServer.controllers;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.persistence.UserRepository;
import com.example.renderFarmServer.service.RenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    @Autowired
    RenderService signUpService;

    private UserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public SignUpController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/sign_up")
    @ResponseBody
    public ResponseEntity<String> signUp(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return signUpService.signUpResponse(user);
    }
}
