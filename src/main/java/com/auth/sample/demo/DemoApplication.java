package com.auth.sample.demo;

import com.auth.sample.demo.exception.ApplicationException;
import com.auth.sample.demo.model.Role;
import com.auth.sample.demo.model.RoleName;
import com.auth.sample.demo.model.User;
import com.auth.sample.demo.repository.RoleRepository;
import com.auth.sample.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@Component
class DataLoader {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        loadRoles();
        saveUser();
    }

    private void saveUser() {
        User user = new User("Admin", "Admin@Admin.com", "123");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ApplicationException("No user role exists"));
        user.setUserRoles(Collections.singleton(userRole));

        userRepository.save(user);

        User user2 = new User("User", "User@User.com", "123");

        user2.setPassword(passwordEncoder.encode(user2.getPassword()));
        Role userRole2 = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new ApplicationException("No user role exists"));
        HashSet<Role> roleSet = new HashSet();
        roleSet.add(userRole);
        roleSet.add(userRole2);
        user2.setUserRoles(roleSet);

        userRepository.save(user2);
    }


    private void loadRoles() {
        roleRepository.save(new Role(RoleName.ROLE_USER));
        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
    }
}
