package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.ParameterService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceParameterReply extends BaseDto {
    private Long id;
    private String name; // Clave de parámetro (único por negocio)
    private String caption; // Texto en edición del parámetro
    private Integer typeValue; // Tipo de parámetro: 0=check, 1=string, 2=integer (auxValue=rango), 3=float (idem), 4=string desplegable (auxValue=valores separados por |)
    private String defaultValue; // Valor por defecto del parámetro
    private String auxValue; // Varios dependiendo de typeValue
    private Boolean required; // Valor requerido en la reserva del servicio

    public ServiceParameterReply(ParameterService ps) {
        super.loadFromObject(ps.getParameter());
        if (ps.getDefaultValue() != null) {
            this.defaultValue = ps.getDefaultValue();
        }
        this.required = ps.getRequired();
    }
}
