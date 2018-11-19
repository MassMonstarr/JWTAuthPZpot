package com.auth.sample.demo.controller;


import com.auth.sample.demo.exception.ApplicationException;
import com.auth.sample.demo.model.Role;
import com.auth.sample.demo.model.RoleName;
import com.auth.sample.demo.model.User;
import com.auth.sample.demo.payload.ApiResponse;
import com.auth.sample.demo.payload.JwtAuthenticationResponse;
import com.auth.sample.demo.payload.LoginRequest;
import com.auth.sample.demo.payload.RegistrationRequest;
import com.auth.sample.demo.repository.RoleRepository;
import com.auth.sample.demo.repository.UserRepository;
import com.auth.sample.demo.security.JwtTokenProvider;
import com.auth.sample.demo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username already exists"), HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "This email address is already in use"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(registrationRequest.getUsername(), registrationRequest.getEmail(), registrationRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new ApplicationException("No user role exists"));

        user.setUserRoles(Collections.singleton(userRole));

        User savedUser = userRepository.save(user);

        URI userLocation = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(savedUser.getUsername()).toUri();


        return ResponseEntity.created(userLocation).body(new ApiResponse(true, "User successfully registered"));
    }


}
