package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.auth.CustomUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserReply extends BaseDto {

	private Long id;
	private String email;
	private String phone;
	private String name;

	public UserReply(UserDto dto) {
		super();
		super.loadFromObject(dto);
	}

	public UserReply(CustomUserDetails user) {
		this.id = user.getUserId();
		this.email = user.getUsername();
		this.phone = user.getUserphone();
		this.name = user.getName();
	}
}
