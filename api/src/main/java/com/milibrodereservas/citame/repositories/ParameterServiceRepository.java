package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.ParameterService;
import com.milibrodereservas.citame.entities.ParameterServiceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParameterServiceRepository extends JpaRepository<ParameterService, ParameterServiceId> {
    List<ParameterService> findByIdIdServiceOrderByPosition(Long idService);
}
