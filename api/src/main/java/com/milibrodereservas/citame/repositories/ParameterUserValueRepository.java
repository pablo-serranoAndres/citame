package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.ParameterUserValue;
import com.milibrodereservas.citame.entities.ParameterUserValueId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterUserValueRepository extends JpaRepository<ParameterUserValue, ParameterUserValueId> {
}
