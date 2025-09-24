package com.milibrodereservas.citame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.milibrodereservas.citame.entities.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
