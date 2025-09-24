package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.Business;
import com.milibrodereservas.citame.entities.Service;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import com.milibrodereservas.citame.repositories.ServiceRepository;
import com.milibrodereservas.citame.util.UtilDate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@org.springframework.stereotype.Service
public class ServiceService extends Base {
    private final static int SERVICE_TYPE_SIMPLE = 1;

    @Autowired
    private ServiceRepository repo;
    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private TimetableService timetableService;

    public ServiceDto getServiceById(Long id) throws ValidationException {
        try {
            Service entity = repo.findById(id).orElseThrow();
            return new ServiceDto(entity);
        } catch (NoSuchElementException e) {
            throw new ValidationException("Service not found", ValidationException.NOT_FOUND, "idService");
        }
    }

    public List<ServiceDto> getAllowedServices(String idBusiness, Long idUser)
            throws ValidationException {
        logger.debug("ServiceService.getAllowedServices {} id = {}", idBusiness, idUser);
        try {
            Business business = businessRepository.findByIdString(idBusiness).orElseThrow();
            List<ServiceDto> reply = new ArrayList<>();
            for (Service item : business.getServices()) {
                if (allowedService(item, business)) {
                    reply.add(new ServiceDto(item));
                }
            }
            return reply;
        } catch (NoSuchElementException e) {
            throw new ValidationException("business not found", ValidationException.NOT_FOUND, "business");
        }
    }
    private boolean allowedService(Service service, Business business) {
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

    public ServiceAvailabilityReply getAvailability(Long idService) {
        try {
            ServiceAvailabilityReply reply = new ServiceAvailabilityReply();

            Service service = repo.findById(idService).orElseThrow();
            reply.setNameService(service.getName());

            int bookingDays = 10; // TODO valor defecto aplicacion
            int prevBookingDays = 0;
            int prevBookingMins = 0;
            if (service.getBookingDays() != null) {
                bookingDays = service.getBookingDays();
            } else if (service.getBusiness().getBookingDays() != null) {
                bookingDays = service.getBusiness().getBookingDays();
            }
            if (service.getPrevBookingDays() != null) {
                prevBookingDays = service.getPrevBookingDays();
            } else if (service.getBusiness().getPrevBookingDays() != null) {
                prevBookingDays = service.getBusiness().getPrevBookingDays();
            }
            if (prevBookingDays == 0) {
                if (service.getPrevBookingMins() != null) {
                    prevBookingMins = service.getPrevBookingMins();
                } else if (service.getBusiness().getPrevBookingMins() != null) {
                    prevBookingMins = service.getBusiness().getPrevBookingMins();
                }
            }
            int bookingStep = service.effectiveBookingStep();

            LocalDate today = LocalDate.now();
            LocalTime startTime = LocalTime.now().plusMinutes(prevBookingMins);
            if ((prevBookingDays >= UtilDate.MINS_OF_DAY) || (startTime.isBefore(LocalTime.now()))) { // la reserva pasa al dia siguiente
                prevBookingDays = 1;
            }
            LocalDate startDate = today.plusDays(prevBookingDays);
            LocalDate endDate = today.plusDays(bookingDays - 1);
            List<TimetableReply> timetable = timetableService.getSchedules(
                    service.getBusiness().getIdString(), startDate, endDate);

            List<ServiceAvailabilityItem> availability = new ArrayList<>();
            reply.setAvailability(availability);
            for (TimetableReply item : timetable) {
                ServiceAvailabilityItem itemDetail = new ServiceAvailabilityItem(item.getDate());
                itemDetail.setDetails(new ArrayList<>());
                for (TimetableDetail detail : item.getDetailTimetable()) {
                    LocalTime time = detail.getStartTime();
                    LocalTime endTime = detail.getEndTime();
                    while (time.isBefore(endTime)) {
                        if (!item.getDate().equals(today) || !time.isBefore(startTime)) {
                            itemDetail.getDetails().add(new ServiceAvailabilityDetail(time, endTime));
                        }
                        time = time.plusMinutes(bookingStep);
                    }
                }
                if (!itemDetail.getDetails().isEmpty()) {
                    availability.add(itemDetail);
                }
            }

            switch (service.getType()) {
                case SERVICE_TYPE_SIMPLE:
                    filterAvailabilitySimpleService(availability, service);
            }
            return reply;
        } catch (NoSuchElementException e) {
            throw new ValidationException("Service not found", ValidationException.NOT_FOUND, "idService");
        }
    }
    private void filterAvailabilitySimpleService(List<ServiceAvailabilityItem> reply, Service service) {
        for (Iterator<ServiceAvailabilityItem> itDay = reply.iterator(); itDay.hasNext(); ) {
            ServiceAvailabilityItem day = itDay.next();
            for (Iterator<ServiceAvailabilityDetail> itDetail = day.getDetails().iterator(); itDetail.hasNext(); ) {
                ServiceAvailabilityDetail item = itDetail.next();
                LocalTime finish = item.getStart().plusMinutes(service.getDuration());
                if (!finish.isAfter(item.getEnd())) {
                    item.setEnd(finish);
                } else {
                    itDetail.remove();
                }
            }
            if (day.getDetails().isEmpty()) {
                itDay.remove();
            }
        }
    }
}
