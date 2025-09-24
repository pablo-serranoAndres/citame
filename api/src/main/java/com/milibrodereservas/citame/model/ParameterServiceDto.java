package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.ParameterService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParameterServiceDto extends BaseDto {
	private ServiceDto service;
	private ParameterDto parameter;
	private Integer position;
	private Boolean required;
	private ParameterDto storeUserParam;
	private ParameterDto recoverUserParam;
	private String defaultValue;

	public ParameterServiceDto(ParameterService parameterService) {
		super();
		super.loadFromObject(parameterService);
		this.service = new ServiceDto(parameterService.getService());
		this.parameter = new ParameterDto(parameterService.getParameter());
		if (parameterService.getStoreUserParam() != null) {
			this.storeUserParam = new ParameterDto(parameterService.getStoreUserParam());
		}
		if (parameterService.getRecoverUserParam() != null) {
			this.recoverUserParam = new ParameterDto(parameterService.getRecoverUserParam());
		}
	}
}
