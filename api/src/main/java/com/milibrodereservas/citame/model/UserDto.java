package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDto extends BaseDto {

	private Long id;
	private String email;
	private String phone;
	private String name;
	private String verification;
	private Date registrationDate;
	private Date deactivationDate;
	private Date emailRegistrationDate;
	private Date emailVerificationDate;

	public UserDto(Long id) {
		this.id = id;
	}

	public UserDto(User user) {
		super();
		super.loadFromObject(user);
	}
}
