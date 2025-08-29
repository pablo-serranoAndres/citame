package com.milibrodereservas.citame.repositories;

import com.milibrodereservas.citame.entities.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    @Query("SELECT t FROM Timetable t WHERE t.business.idString = :business " +
            "AND (t.startDate IS NULL OR t.startDate <= :endDate) " +
            "AND (t.endDate IS NULL OR t.endDate >= :startDate) " +
            "ORDER BY CASE WHEN t.startDate IS NULL AND t.endDate IS NULL THEN 1 ELSE 0 END ASC, " +
                     "CASE WHEN t.startDate IS NULL THEN 1 ELSE 0 END ASC, " +
                     "t.startDate DESC, t.endDate DESC")
    List<Timetable> findByBusinessAndDates(@Param("business") String business,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
}
