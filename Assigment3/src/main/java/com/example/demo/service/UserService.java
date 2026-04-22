package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(Long id);

    // Phương thức tùy chỉnh
    List<User> findByRole(String role);

    List<User> searchByKeyword(String keyword);

    // Bạn có thể thêm các phương thức khác nếu cần
}
