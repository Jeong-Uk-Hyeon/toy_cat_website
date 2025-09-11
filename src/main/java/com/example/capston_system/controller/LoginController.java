package com.example.capston_system.controller;

import com.example.capston_system.entity.User;
import com.example.capston_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> request) {
        String userid = request.get("userid");
        String userpass = request.get("userpass");

        Map<String, Object> response = new HashMap<>();

        // DB에서 해당 유저 찾기
        User user = userRepository.findByUserid(userid).orElse(null);

        if (user != null && user.getUserpass().equals(userpass)) {
            response.put("success", true);
        } else {
            response.put("success", false);
        }

        return response;
    }
}


