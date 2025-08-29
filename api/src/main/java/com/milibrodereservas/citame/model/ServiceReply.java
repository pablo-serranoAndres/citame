package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Services;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ServiceReply extends BaseDto {
    private Long id;
    private String name;
    private Integer type;

    public ServiceReply(ServiceDto service) {
        super();
        this.id = service.getId();
        this.name = service.getName();
        this.type = service.getType();
    }
}
