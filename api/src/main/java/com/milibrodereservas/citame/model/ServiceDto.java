package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Service;
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
	private Integer bookingStep;
	private Integer bookingDays;
	private Date startDate;
	private Date endDate;
	private Integer prevBookingDays;
	private Integer prevBookingMins;
	private Integer duration;

	public ServiceDto(Service service) {
		super();
		super.loadFromObject(service);
		if (service.getBusiness() != null) {
			business = new BusinessDto(service.getBusiness(), false);
		}
	}

	public Integer effectiveBookingStep() {
		if (bookingStep != null) {
			return bookingStep;
		} else if (business.getBookingStep() != null) {
			return business.getBookingStep();
		} else {
			return 1;
		}
	}
}
