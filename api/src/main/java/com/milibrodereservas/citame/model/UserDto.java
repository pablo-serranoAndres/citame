package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
	private Boolean mailMessages;

	private List<UserBusinessDto> userBusinesses;

	public UserDto(Long id) {
		this.id = id;
	}

	public UserDto(User user) {
		super();
		super.loadFromObject(user);

		userBusinesses = convertToListDto(user.getUserBusinesses(), UserBusinessDto.class);
	}
}
