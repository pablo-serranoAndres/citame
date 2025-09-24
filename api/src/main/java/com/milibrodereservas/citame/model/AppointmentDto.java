package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Appointment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class AppointmentDto extends BaseDto {
	private Long id;
	private ServiceDto service;
	private UserDto user;
	private LocalDate bookingDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalDateTime created;
	private LocalDateTime modificated;
	private LocalDateTime finished;
	private boolean confirmed;
	private boolean realized;
	private boolean notPresented;
	private boolean cancelled;

	public AppointmentDto(Appointment appointment) {
		super();
		super.loadFromObject(appointment);
		if (appointment.getService() != null) {
			service = new ServiceDto(appointment.getService());
		}
		if (appointment.getUser() != null) {
			user = new UserDto(appointment.getUser());
		}
	}
}
