package com.milibrodereservas.citame.entities;

import com.milibrodereservas.citame.model.AppointmentDto;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table (name = "appointments")
@TableGenerator(
		name = "id_appointment",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_appointment",
		allocationSize = 1
)
public class Appointment implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_appointment")
	private Long id;
	@ManyToOne
	@JoinColumn(name="idService", nullable = false)
	private Service service; // Servicio que se reserva
	@ManyToOne
	@JoinColumn(name="idUser", nullable = false)
	private User user; // Usuario/cliente que reserva
	@Column(nullable = false)
	private LocalDate bookingDate; // Fecha de la reserva
	@Column(nullable = false)
	private LocalTime startTime; // Hora inicio servicio
	@Column(nullable = false)
	private LocalTime endTime; // Hora término servicio
	@Column(nullable = false)
	private LocalDateTime created; // Fecha y hora registro reserva
	private LocalDateTime modificated; // Fecha y hora última modificación
	private LocalDateTime finished; // Fecha y hora cierre registro (realizado,anulado, no presentado...)
	private boolean confirmed; // Reserva confirmada por negocio
	private boolean realized; // Servicio realizado
	private boolean notPresented; // Cliente no se presentó al servicio
	private boolean cancelled; // Reserva anulada

	public Appointment() {
		super();
		this.created = LocalDateTime.now();
	}

	public Appointment(AppointmentDto dto) {
		this();
		dto.storeInObject(this);
	}
}
