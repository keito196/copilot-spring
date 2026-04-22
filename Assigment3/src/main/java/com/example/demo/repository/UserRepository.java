package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email (vì email là unique)
    Optional<User> findByEmail(String email);

    // Tìm user theo role
    List<User> findByRole(String role);

    // Tìm kiếm user theo từ khóa trong name hoặc email
    @Query("SELECT u FROM User u WHERE u.name LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> searchByKeyword(String keyword);

    // Bạn có thể thêm các phương thức tùy chỉnh khác nếu cần
}
