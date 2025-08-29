package com.milibrodereservas.citame.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity implementation class for Entity: User
 *
 */

@Data
@ToString(exclude = {"services", "timetables"})
@Entity
@Table (name = "Business")
@TableGenerator(
		name = "id_business",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_business",
		allocationSize = 1
)
public class Business implements Serializable {


	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_business")
	private Long id;
	@Column(length=32)
	private String idString; // Identificador visible único del negocio
	@Column(length=256)
	private String email; // Correo electrónico (puede estar repetido y no ser válido)
	@Column(length=16)
	private String phone; // Teléfono (puede estar repatido y no ser válido)
	@Column(length=80, nullable=false)
	private String name; // Nombre negocio
	private Date registrationDate; // Fecha alta
	private Date deactivationDate; // Fecha baja (si no null el negocio está desactivado)
	private Integer bookingDays; // Días posibles de reservas anticipadas (null sin limite)

	@OneToMany(mappedBy = "business", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OrderBy("position")
	private List<Services> services = new ArrayList<>();
	@OneToMany(mappedBy = "business", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Timetable> timetables = new ArrayList<>();

	public Business() {
		super();
		this.registrationDate = new Date();
	}

}
