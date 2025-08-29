package com.milibrodereservas.citame.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity implementation class for Entity: User
 *
 */

@Data
@NoArgsConstructor
@ToString(exclude = "business")
@Entity
@Table (name = "timetable")
@TableGenerator(
		name = "id_timetable",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_timetable",
		allocationSize = 1
)
public class Timetable implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_timetable")
	private Long id;
	@ManyToOne
	@JoinColumn(name="idBusiness")
	private Business business; // Negocio al que pertenece el horario
	@Column(length=80)
	private String name; // Nombre horario
	@Column(length=32)
	private String validity; // Expresión validez horario (null siempre) separados por comas: M-T-W-R-F-S-U (días de la semana)
	private LocalTime opening; // Hora de apertura
	private LocalTime closing; // Hora de cierre
	private LocalDate startDate; // Fecha comienzo validez horario (NULL sin limite)
	private LocalDate endDate; // Fecha final validez horario (NULL sin limite)
}
