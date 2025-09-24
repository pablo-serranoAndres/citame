package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByIdString(String idString);
}
