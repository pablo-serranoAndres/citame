package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Message;
import com.milibrodereservas.citame.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageDto extends BaseDto {
	private Long id;
	private AppointmentDto appointment;
	private UserDto user;
	private BusinessDto business;
	private UserDto userBusiness;
	private Boolean fromUser;
	private LocalDateTime created;
	private LocalDateTime showed;
	private LocalDateTime emailed;
	private LocalDateTime deleted;
	private String message;

	public MessageDto(Message message) {
		super();
		super.loadFromObject(message);
		if (message.getAppointment() != null) {
			appointment = new AppointmentDto(message.getAppointment());
		}
		user = new UserDto(message.getUser());
		business = new BusinessDto(message.getBusiness());
		if (message.getUserBusiness() != null) {
			userBusiness = new UserDto(message.getUserBusiness());
		}
	}

	public MessageDto(AppointmentDto appointment,
					  UserDto user,
					  BusinessDto business,
					  UserDto userBusiness,
					  Boolean fromUser,
					  String message) {
		super();
		this.appointment = appointment;
		this.user = user;
		this.business = business;
		this.userBusiness = userBusiness;
		this.fromUser = fromUser;
		this.message = message;
	}
}
