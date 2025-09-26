package com.milibrodereservas.citame.entities;

import com.milibrodereservas.citame.model.ParameterServiceDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table (name = "parameters_service")
public class ParameterService implements Serializable {
	@EmbeddedId
	private ParameterServiceId id;
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idService")
	@JoinColumn(name = "idService", referencedColumnName = "id")
	private Service service;
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idParameter")
	@JoinColumn(name = "idParameter", referencedColumnName = "id")
	private Parameter parameter;
	private Integer position; // Orden al preguntar
	private Boolean required; // Valor requerido en la reserva del servicio
	@ManyToOne
	@JoinColumn(name="storeUserParam")
	private Parameter storeUserParam; // Si no null parámetro de usuario donde se guarda el valor del parámetro de reserva
	@ManyToOne
	@JoinColumn(name="recoverUserParam")
	private Parameter recoverUserParam; // Si no null parámetro de usuario de donde se recupera el valor del parámetro de reserva
	@Column(length=512)
	private String defaultValue; // Valor por defecto del parámetro al reservar este servicio (prioritario al valor defecto del parámetro)

	public ParameterService(ParameterServiceDto dto) {
		this();
		dto.storeInObject(this);
	}
}
