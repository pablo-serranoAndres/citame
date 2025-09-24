package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Business;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BusinessDto extends BaseDto {
	private Long id;
	private String idString;
	private String email;
	private String phone;
	private String name;
	private Date registrationDate;
	private Date deactivationDate;
	private Integer bookingDays;
	private Integer bookingStep;

	public BusinessDto(Business business) {
		super();
		super.loadFromObject(business);
	}

	public BusinessDto(Long id) {
		super();
		this.id = id;
	}
}
