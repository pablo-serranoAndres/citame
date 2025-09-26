package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.UserBusiness;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserBusinessDto extends BaseDto {
    private UserBusinessIdDto id;
    // No se puede tener user y business porque al instanciar desde dto crea un bucle infinito

    public UserBusinessDto(UserBusiness entity) {
        super();
        super.loadFromObject(entity);
        id = new UserBusinessIdDto(entity.getId());
    }
}
