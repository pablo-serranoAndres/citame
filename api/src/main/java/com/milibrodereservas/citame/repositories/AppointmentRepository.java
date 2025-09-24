package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
