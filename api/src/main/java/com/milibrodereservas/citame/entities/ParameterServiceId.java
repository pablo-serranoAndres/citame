package com.milibrodereservas.citame.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class ParameterServiceId implements Serializable {
	private Long idService;
	private Long idParameter;
}
