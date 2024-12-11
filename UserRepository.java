package com.project.Bootcamp.repository;

import com.project.Bootcamp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by username
    Optional<User> findByUsername(String username);

    // Find user by email and verification code
    Optional<User> findByEmailAndVerificationCode(String email, String verificationCode);

    // Check if a user with the given email exists
    boolean existsByEmail(String email);

    // Check if a user with the given username exists
    boolean existsByUsername(String username);
}
