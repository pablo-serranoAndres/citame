package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Services;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ServiceDto extends BaseDto {
	private Long id;
	private BusinessDto business;
	private String name;
	private Integer type;
	private Integer position;
	private Integer bookingDays;
	private Date startDate;
	private Date endDate;

	public ServiceDto(Services service) {
		super();
		super.loadFromObject(service);
		if (service.getBusiness() != null) {
			business = new BusinessDto(service.getBusiness());
		}
	}
}
