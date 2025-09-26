package com.milibrodereservas.citame.entities;

import com.milibrodereservas.citame.model.UserBusinessIdDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserBusinessId implements Serializable {
    private Long idUser;
    private Long idBusiness;

    public UserBusinessId(UserBusinessIdDto dto) {
        dto.storeInObject(this);
    }
}
