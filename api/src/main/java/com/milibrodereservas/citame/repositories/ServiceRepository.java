package com.milibrodereservas.citame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.milibrodereservas.citame.entities.Services;

public interface ServiceRepository extends JpaRepository<Services, Long> {
}
