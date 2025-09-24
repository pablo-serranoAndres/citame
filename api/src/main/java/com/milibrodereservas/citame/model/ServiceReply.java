package com.milibrodereservas.citame.model;

import lombok.Data;
import lombok.NoArgsConstructor;

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
