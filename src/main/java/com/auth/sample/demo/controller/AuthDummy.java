package com.auth.sample.demo.controller;

import com.auth.sample.demo.repository.UserRepository;
import com.auth.sample.demo.security.CurrentUser;
import com.auth.sample.demo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthDummy {

    private final UserRepository userRepository;

    @Autowired
    public AuthDummy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String hello(@CurrentUser UserPrincipal principal) {
        if (principal != null) {
            return "Hello there, " + principal.getUsername();
        } else {
            return "booo";
        }
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String helloUser() {
        return "Hello, you are a user indeed!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "Hello, you are an admin indeed!";
    }

}
