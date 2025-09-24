package com.milibrodereservas.citame.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table (name = "parameters_user_value")
public class ParameterUserValue implements Serializable {
	@EmbeddedId
	private ParameterUserValueId id;
	@Column(length=512)
	private String value; // Valor del parámetro para el usuario
	@Column(nullable = false)
	private LocalDateTime created; // Fecha y hora creación valor
	private LocalDateTime modificated; // Fecha y hora última modificación valor

	public ParameterUserValue() {
		super();
		this.created = LocalDateTime.now();
	}
}
