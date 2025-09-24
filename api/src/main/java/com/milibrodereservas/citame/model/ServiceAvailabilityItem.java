package com.milibrodereservas.citame.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ServiceAvailabilityItem extends BaseDto {
    private LocalDate date;
    private List<ServiceAvailabilityDetail> details;

    public ServiceAvailabilityItem(LocalDate date) {
        this.date = date;
    }
}
