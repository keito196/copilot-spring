package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.entity.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @PostMapping("/register")
    public ResponseEntity<Long> registerUser(@Valid @RequestBody CreateUserRequest request) {
        // Placeholder implementation
        log.info("Registering user: {}", request);
        return ResponseEntity.ok(1L); // Placeholder user ID
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        // Placeholder implementation
        log.info("Getting user by id: {}", id);
        return ResponseEntity.ok(new User()); // Placeholder user
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        // Placeholder implementation
        log.info("Getting all users with pagination: {}", pageable);
        return ResponseEntity.ok(Page.empty()); // Placeholder empty page
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody User user) {
        // Placeholder implementation
        log.info("Updating user with id: {}, data: {}", id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Placeholder implementation
        log.info("Deleting user with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
