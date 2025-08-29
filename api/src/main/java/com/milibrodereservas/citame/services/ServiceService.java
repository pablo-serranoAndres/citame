package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.Business;
import com.milibrodereservas.citame.entities.Services;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.ServiceDto;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import com.milibrodereservas.citame.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceService extends Base {
    @Autowired
    private ServiceRepository repository;
    @Autowired
    private BusinessRepository businessRepository;

    public List<ServiceDto> getAllowedServices(String idBusiness, Long idUser)
            throws ValidationException {
        logger.debug("ServiceService.getAllowedServices {} id = {}", idBusiness, idUser);
        try {
            Business business = businessRepository.findByIdString(idBusiness).orElseThrow();
            List<ServiceDto> reply = new ArrayList<>();
            for (Services item : business.getServices()) {
                if (allowedService(item, business)) {
                    reply.add(new ServiceDto(item));
                }
            }
            return reply;
        } catch (NoSuchElementException e) {
            throw new ValidationException("business not found", ValidationException.NOT_FOUND, "business");
        }
    }
    private boolean allowedService(Services service, Business business) {
        int daysToStart = (int) ChronoUnit.DAYS.between(LocalDate.now(), service.getStartDate());
        if ((business.getBookingDays() != null)
                && (daysToStart >= business.getBookingDays())) {
            return false;
        }

        if ((service.getBookingDays() != null) && (daysToStart >= service.getBookingDays())) {
            return false;
        }
        return true;
    }
}
