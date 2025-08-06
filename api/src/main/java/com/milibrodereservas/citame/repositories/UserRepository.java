package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrPhone(String email, String phone);
}
