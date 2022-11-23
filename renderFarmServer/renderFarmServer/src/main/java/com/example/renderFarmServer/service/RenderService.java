package com.example.renderFarmServer.service;

import com.example.renderFarmServer.model.User;
import com.example.renderFarmServer.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RenderService {

    @Autowired
    private UserRepository userRepository;

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

    public ResponseEntity<String> signUpResponse(User user) {
        if(!userRepository.existsById(user.getUser_id())) {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(signUpSuccessful);
        } else return ResponseEntity.status(400).body(signUpFailed);
    }

}
