package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testFindByEmail_NotFound() {
        // Act
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("new@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void testFindById() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        User saved = userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findById(saved.getId());

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User user = new User();
        user.setUsername("deleteuser");
        user.setEmail("delete@example.com");
        User saved = userRepository.save(user);
        Long userId = saved.getId();

        // Act
        userRepository.deleteById(userId);

        // Assert
        Optional<User> deleted = userRepository.findById(userId);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        // Act
        var users = userRepository.findAll();

        // Assert
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }
}
