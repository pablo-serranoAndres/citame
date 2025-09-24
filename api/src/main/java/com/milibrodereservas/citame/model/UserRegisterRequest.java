package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.model.validations.EmailOrPhoneNotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@EmailOrPhoneNotNull(message = "email or phone are mandatory")
public class UserRegisterRequest implements Serializable {
	@Schema(description = "email para loguear (alternativo a phone)", example = "guest@citame.com")
	@Email(message = "email format not valid")
	@Size(max = 100, message = "email cannot be longer than 100 characters")
	private String email;
	@Schema(description = "teléfono para loguear (alternativo a email)", example = "+34666999666")
	@Size(max = 20, message = "phone cannot be longer than 20 characters")
	private String phone;
	@Schema(description = "nombre usuario", example = "Pepe López")
	@NotBlank(message = "name is mandatory")
	@Size(max = 50, message = "name cannot be longer than 50 characters")
	private String name;
	@Schema(description = "contraseña sin encriptar", example = "p4ssw0rd123&s3gur4")
	@NotBlank(message = "password is mandatory")
	@Size(min = 6, message = "password must be at least 6 characters long")
	private String password;
}
