package com.milibrodereservas.citame.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ParameterServiceValueId implements Serializable {
	private Long idAppointment;
	private Long idParameter;
}
