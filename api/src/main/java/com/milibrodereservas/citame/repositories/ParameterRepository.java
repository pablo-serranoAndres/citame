package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {
}
