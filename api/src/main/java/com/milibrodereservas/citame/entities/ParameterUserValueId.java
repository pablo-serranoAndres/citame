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
public class ParameterUserValueId implements Serializable {
	private Long idUser;
	private Long idParameter;
}
