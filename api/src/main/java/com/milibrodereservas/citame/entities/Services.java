package com.milibrodereservas.citame.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entity implementation class for Entity: Service
 *
 */

@Data
@Entity
@Table (name = "services")
@TableGenerator(
		name = "id_service",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_service",
		allocationSize = 1
)
public class Services implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_service")
	private Long id;
	@ManyToOne
	@JoinColumn(name="idBusiness")
	private Business business; // Negocio al que pertenece el servicio
	@Column(length=80, nullable=false)
	private String name; // Nombre servicio
	private Integer type; // Tipo funcionamiento servicio
	private Integer position; // Orden al mostrar
	private Integer bookingDays; // Días posibles de reserva anticipada (NULL sin límite)
	private LocalDate startDate; // Fecha inicio servicio. Reservas no admitidas antes de esta fecha
	private LocalDate endDate; // Fecha fin servicio. Reservas no admitidas después de esta fecha (si null sin límite)

	public Services() {
		super();
		this.startDate = LocalDate.now();
	}

}
