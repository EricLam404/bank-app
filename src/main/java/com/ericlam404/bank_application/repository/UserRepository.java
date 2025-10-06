package com.ericlam404.bank_application.repository;

import com.ericlam404.bank_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);

    Optional<User> findByEmail(String email);
}
