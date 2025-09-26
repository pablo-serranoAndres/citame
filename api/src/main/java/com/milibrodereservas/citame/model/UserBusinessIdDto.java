package com.milibrodereservas.citame.model;

import com.milibrodereservas.citame.entities.UserBusinessId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserBusinessIdDto extends BaseDto {
    private Long idUser;
    private Long idBusiness;

    public UserBusinessIdDto(UserBusinessId entity) {
        super();
        super.loadFromObject(entity);
    }
}
