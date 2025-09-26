package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.Appointment;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.AppointmentDto;
import com.milibrodereservas.citame.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AppointmentService extends Base {
    @Autowired
    private AppointmentRepository repo;

    public AppointmentDto findById(Long id) {
        try {
            return new AppointmentDto(repo.findById(id).orElseThrow());
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public AppointmentDto create (AppointmentDto appointment) {
        Appointment entity = new Appointment(appointment);
        entity = repo.save(entity);
        if (entity != null) {
            return new AppointmentDto(entity);
        } else {
            return null;
        }
    }
}
