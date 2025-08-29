package com.milibrodereservas.citame.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ServiceAvailabilityReply extends BaseDto {
    private LocalDate date;
    private List<ServiceAvailabilityDetail> details;

    public ServiceAvailabilityReply(LocalDate date) {
        this.date = date;
    }
}
