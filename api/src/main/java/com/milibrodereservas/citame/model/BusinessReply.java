package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Business;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessReply extends BaseDto {
	private Long id;
	private String idString;
	private String email;
	private String phone;
	private String name;

	public BusinessReply(Business business) {
		super();
		super.loadFromObject(business);
	}

	public BusinessReply(BusinessDto dto) {
		super();
		super.loadFromObject(dto);
	}
}
