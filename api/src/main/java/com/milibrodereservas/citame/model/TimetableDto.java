package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Timetable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class TimetableDto extends BaseDto {
	private Long id;
	private BusinessDto business;
	private String name;
	private String validity;
	private LocalTime opening;
	private LocalTime closing;
	private Date startDate;
	private Date endDate;

	public TimetableDto(Timetable timetable) {
		super();
		super.loadFromObject(timetable);
		if (timetable.getBusiness() != null) {
			business = new BusinessDto(timetable.getBusiness());
		}
	}
}
