package com.milibrodereservas.citame.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity implementation class for Entity: User
 *
 */

@Data
@Entity
@Table (name = "user_login")
@TableGenerator(
		name = "id_user_login",
		table = "hibernate_sequences",
		pkColumnName = "sequence_name",
		valueColumnName = "next_val",
		pkColumnValue = "id_user_login",
		allocationSize = 1
)
public class User implements Serializable {

	private static final long serialVersionUID = 980703184498702056L;

	public static final int LENGTH_VERIFICACION = 16;
	public static final int LENGTH_FIELD_NAME = 32;
	public static final int LENGTH_FIELD_EMAIL = 256;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id_user_login")
	private Long id;
	@Column(length=256, nullable=false, unique=true)
	private String email;
	@Column(length=16, nullable=false, unique=true)
	private String phone;
	@Column(length=32, nullable=false)
	private String name;
	@Column(length=128, nullable=true)  // longitud del hash con SHA-512 / hex = 128 bytes
	private String password;
	@Column(length=16, nullable=true)
	private String verification;
	private Date registrationDate;
	private Date deactivationDate;
	private Date emailRegistrationDate;
	private Date emailVerificationDate;

	public User() {
		super();
		this.registrationDate = new Date();
		this.verification = RandomStringUtils.randomAlphanumeric(LENGTH_VERIFICACION);;
	}

}
