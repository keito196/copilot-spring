package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // POST /api/users: Create user
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = mapToEntity(userDTO);
        User savedUser = userService.save(user);
        UserDTO responseDTO = mapToDTO(savedUser);
        return ResponseEntity.ok(responseDTO);
    }

    // GET /api/users/{id}: Fetch user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            UserDTO userDTO = mapToDTO(user.get());
            return ResponseEntity.ok(userDTO);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    // GET /api/users: Retrieve all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // PUT /api/users/{id}: Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword()); // Lưu ý: nên mã hóa password
            user.setRole(userDTO.getRole());
            User updatedUser = userService.save(user);
            UserDTO responseDTO = mapToDTO(updatedUser);
            return ResponseEntity.ok(responseDTO);
        } else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }
    
    // DELETE /api/users/{id}: Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    // Helper methods for mapping
    private User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword()); // Lưu ý: trong ứng dụng thực, không trả về password
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
