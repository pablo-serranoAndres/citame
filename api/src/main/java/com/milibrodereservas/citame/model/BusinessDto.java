package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Business;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private Boolean mailMessages;

	private List<ServiceDto> services;
	private List<TimetableDto> timetables;
	private List<UserBusinessDto> userBusinesses;

	public BusinessDto(Business business) {
		this(business, true);
	}
	public BusinessDto(Business business, boolean iterative) {
		super();
		super.loadFromObject(business);
		if (iterative) {
			services = BaseDto.convertToListDto(business.getServices(), ServiceDto.class);
			timetables = BaseDto.convertToListDto(business.getTimetables(), TimetableDto.class);
		}
		userBusinesses = BaseDto.convertToListDto(business.getUserBusinesses(), UserBusinessDto.class);
	}

	public BusinessDto(Long id) {
		super();
		this.id = id;
	}
}
