package com.aacode.bietdiet.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "123456";

        String encryptedPassword = encoder.encode(password);

        System.out.println(encryptedPassword);
    }
}