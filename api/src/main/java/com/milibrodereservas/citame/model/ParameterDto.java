package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.Parameter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParameterDto extends BaseDto {
	private Long id;
	private BusinessDto business;
	private String name;
	private String caption;
	private Integer typeValue;
	private String defaultValue;
	private String auxValue; // Varios dependiendo de typeValue

	public ParameterDto(Parameter parameter) {
		super();
		super.loadFromObject(parameter);
		if (parameter.getBusiness() != null) {
			business = new BusinessDto(parameter.getBusiness());
		}
	}
}
