package com.milibrodereservas.citame.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class TimetableReply extends BaseDto{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(type = "string", format = "date", example = "2025-08-24", description = "Fecha del horario (yyyy-mm-dd)")
    private LocalDate date;
    @Schema(description="Detalle de horarios de la jornada")
    List<TimetableDetail> detailTimetable;
}
