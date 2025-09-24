package com.milibrodereservas.citame.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ServiceAvailabilityReply extends BaseDto {
    private String nameService;
    private List<ServiceAvailabilityItem> availability;
}
