package com.milibrodereservas.citame.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParameterValueRequest extends BaseDto {
    @Schema(description = "Identificador del parámetro")
    private Long idParameter;
    @Schema(description = "Valor del parámetro")
    private String value;
}
