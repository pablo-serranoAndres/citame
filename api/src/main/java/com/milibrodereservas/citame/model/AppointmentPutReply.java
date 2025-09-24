package com.milibrodereservas.citame.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.milibrodereservas.citame.auth.CustomUserDetails;
import com.milibrodereservas.citame.entities.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AppointmentPutReply extends BaseDto{
    @Schema(description = "Identificador cita. Si null no admitida")
    private Long id;
    @Schema(description = "Negocio reservado")
    private BusinessReply business;
    @Schema(description = "Servicio reservado")
    private ServiceReply service;
    @Schema(description = "Usuario reserva")
    private UserReply user;
    @Schema(description = "Fecha reserva")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;
    @Schema(description = "Hora reserva")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @Schema(description = "Hora prevista final")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    @Schema(description = "Fecha-hora registro cita")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @Schema(description = "Fecha-hora última modificación cita")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modificated;
    @Schema(description = "Fecha-hora registro finalización cita")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finished;
    @Schema(description = "Cita confirmada por negocio")
    private Boolean confirmed;
    @Schema(description = "Servicio realizado")
    private Boolean realized;
    @Schema(description = "Usuario no presentado a negocio")
    private Boolean notPresented;
    @Schema(description = "Cita cancelada")
    private Boolean cancelled;
    @Schema(description = "Lista de citas disponibles (si no aceptada)")
    private List<ServiceAvailabilityItem> availability;

    public AppointmentPutReply(Appointment appointment) {
        super();
        super.loadFromObject(appointment);
    }

    public AppointmentPutReply(AppointmentDto dto, CustomUserDetails user) {
        this.id = dto.getId();
        this.business = new BusinessReply(dto.getService().getBusiness());
        this.service = new ServiceReply(dto.getService());
        this.user = new UserReply(user);
        this.bookingDate = dto.getBookingDate();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.created = dto.getCreated();
        this.modificated = dto.getModificated();
        this.finished = dto.getFinished();
        this.confirmed = dto.getConfirmed();
        this.realized = dto.getRealized();
        this.notPresented = dto.getNotPresented();
        this.cancelled = dto.getCancelled();
    }
}
