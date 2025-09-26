package com.milibrodereservas.citame.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MessageRequest implements Serializable {
	@Schema(description = "Identificador del usuario al que se envia el mensaje (si el origen es un negocio)")
	private Long idUser;
	@Schema(description = "Identificador del negocio al que se envia el mensaje (si idUser especificado) o recibe (si idUser no especificado)")
	private Long idBusiness;
	@Schema(description = "Identificador del usuario del negocio (opcional)")
	private Long idUserBusiness;
	@Schema(description = "Identificador de la cita asociada a la conversación (opcional)")
	private Long idAppointment;
	@Schema(description = "Mensaje")
	private String message;
}
