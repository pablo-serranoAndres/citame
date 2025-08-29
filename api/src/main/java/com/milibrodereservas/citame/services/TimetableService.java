package com.milibrodereservas.citame.services;

import com.milibrodereservas.citame.entities.Timetable;
import com.milibrodereservas.citame.global.Base;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.TimetableRepository;
import com.milibrodereservas.citame.util.UtilDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TimetableService extends Base {
    @Autowired
    private TimetableRepository repository;

    public List<TimetableReply> getSchedules(String idBusiness, LocalDate startDate, LocalDate endDate) {
        logger.debug("TimetableService.getSchedules {} - {} - {}", idBusiness, startDate, endDate);
        List<TimetableReply> reply = new ArrayList<>();
        List<Timetable> timetables = repository.findByBusinessAndDates(idBusiness, startDate, endDate);
        for (LocalDate date = startDate ; date.compareTo(endDate) <= 0 ; date = date.plusDays(1)) {
            List<Timetable> timetable = getTimesByDate(timetables, date);
            TimetableReply item = new TimetableReply();
            item.setDate(date);
            item.setDetailTimetable(new ArrayList<>());
            for (Timetable t : timetable) {
                TimetableDetail detail = new TimetableDetail();
                detail.setStartTime(t.getOpening());
                detail.setEndTime(t.getClosing());
                item.getDetailTimetable().add(detail);
            }
            if (!item.getDetailTimetable().isEmpty()) {
                reply.add(item);
            }
        }
        return reply;
    }
    private List<Timetable> getTimesByDate(List<Timetable> timetables, LocalDate date) {
        List<Timetable> reply = new ArrayList<>();
        for (Iterator<Timetable> iter = timetables.iterator() ; iter.hasNext(); ) {
            Timetable t = iter.next();
            if ((t.getStartDate() == null) || (t.getStartDate().compareTo(date) <= 0)) {
                LocalDate target = t.getStartDate();
                while ((target == null)
                        || ((t.getStartDate() != null) && (target.compareTo(t.getStartDate()) == 0))) {
                    if (timetableValid(t, date)) {
                        reply.add(t);
                    }
                    if (iter.hasNext()) {
                        t = iter.next();
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return reply;
    }
    private boolean timetableValid(Timetable t, LocalDate date) {
        if ((t.getEndDate() != null) && (t.getEndDate().compareTo(date) < 0)) {
            return false;
        }
        return UtilDate.validDayOfWeek(t.getValidity(), date);
    }
}
