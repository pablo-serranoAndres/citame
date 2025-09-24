package com.milibrodereservas.citame.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AppointmentRequest extends BaseDto {
    @Schema(description = "Identificador del servicio", example = "1")
    private Long idService;
    @Schema(description = "Fecha de la reserva", example = "2025-09-15")
    private LocalDate bookingDate;
    @Schema(description = "Hora de la cita", example = "17:25")
    private LocalTime startTime;
    @Schema(description = "Lista valores parámetros reserva")
    private List<ParameterValueRequest> parametersValues;
}
