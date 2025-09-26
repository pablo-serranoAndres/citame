package com.milibrodereservas.citame.entities;

import com.milibrodereservas.citame.model.MessageDto;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table (name = "messages")
@TableGenerator(
		name = "id_message",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_message",
		allocationSize = 1
)
public class Message implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_message")
	private Long id;
	@ManyToOne
	@JoinColumn(name="idAppointment")
	private Appointment appointment; // Reserva asociada a la conversación
	@ManyToOne
	@JoinColumn(name="idUser", nullable = false)
	private User user; // Usuario/cliente de la conversación
	@ManyToOne
	@JoinColumn(name="idBusiness", nullable = false)
	private Business business; // Negocio de la conversación
	@ManyToOne
	@JoinColumn(name="idUserBusiness")
	private User userBusiness; // Usuario del negocio al que pertenece la conversación
	private Boolean fromUser; // Origen del mensaje (false=negocio, true=usuario)
	@Column(nullable = false)
	private LocalDateTime created; // Fecha y hora escritura mensaje
	private LocalDateTime showed; // Fecha y hora mostrado mensaje
	private LocalDateTime emailed; // Fecha y hora envío por email
	private LocalDateTime deleted; // Fecha y hora borrado mensaje
	@Column(nullable = false)
	private String message; // Mensaje

	public Message() {
		super();
		this.created = LocalDateTime.now();
	}

	public Message(MessageDto dto) {
		this();
		dto.storeInObject(this);
	}
}
