package com.milibrodereservas.citame.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table (name = "parameters_service_value")
public class ParameterServiceValue implements Serializable {
	@EmbeddedId
	private ParameterServiceValueId id;
	@Column(length=512)
	private String value; // Valor del parámetro para el usuario
	@Column(nullable = false)
	private LocalDateTime created; // Fecha y hora creación valor
	private LocalDateTime modificated; // Fecha y hora última modificación valor

	public ParameterServiceValue() {
		super();
		this.created = LocalDateTime.now();
	}
}
