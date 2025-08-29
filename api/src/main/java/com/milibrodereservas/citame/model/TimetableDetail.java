package com.milibrodereservas.citame.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TimetableDetail extends BaseDto{
    @Schema(type = "string", format = "time", example = "10:00", description = "Hora apertura (hh:mm)")
    private LocalTime startTime;
    @Schema(type = "string", format = "time", example = "14:30", description = "Hora cierre (hh:mm)")
    private LocalTime endTime;
}
