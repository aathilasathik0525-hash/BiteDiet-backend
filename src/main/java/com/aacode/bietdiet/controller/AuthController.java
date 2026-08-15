package com.aacode.bietdiet.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.aacode.bietdiet.models.LoginRequest;
import com.aacode.bietdiet.models.Users;
import com.aacode.bietdiet.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody LoginRequest loginUser) {

        Map<String, Object> response = new HashMap<>();

        Optional<Users> user =
                userRepository.findByEmail(loginUser.getEmail());

        if (user.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }

        Users existingUser = user.get();

        if (!passwordEncoder.matches(
                loginUser.getPassword(),
                existingUser.getPassword())) {

            response.put("success", false);
            response.put("message", "Invalid password");
            return response;
        }

        response.put("success", true);
        response.put("message", "Login successful");

        response.put("id", existingUser.getId());
        response.put("fullName", existingUser.getName());
        response.put("email", existingUser.getEmail());
        response.put("patientType", existingUser.getPatientType());

        return response;
    }

    @PutMapping("/patient-type")
    public String updatePatientType(
            @RequestParam String email,
            @RequestParam String patientType) {

        Optional<Users> user =
                userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return "User not found";
        }

        Users existingUser = user.get();

        existingUser.setPatientType(patientType);

        userRepository.save(existingUser);

        return "Patient type saved successfully";
    }
}