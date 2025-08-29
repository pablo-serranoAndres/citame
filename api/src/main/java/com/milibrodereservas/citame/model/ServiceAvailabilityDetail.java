package com.milibrodereservas.citame.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ServiceAvailabilityDetail extends BaseDto {
    private Date start;
    private Date end;
}
