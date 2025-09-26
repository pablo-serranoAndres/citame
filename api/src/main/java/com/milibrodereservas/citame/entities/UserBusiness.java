package com.milibrodereservas.citame.entities;

import com.milibrodereservas.citame.model.UserBusinessDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude = {"user", "business"})
@Entity
@Table(name = "user_business")
public class UserBusiness {
    @EmbeddedId
    private UserBusinessId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUser")
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idBusiness")
    @JoinColumn(name = "idBusiness", referencedColumnName = "id")
    private Business business;

    public UserBusiness(UserBusinessDto dto) {
        dto.storeInObject(this);
        // como user y business no estan en dto
        user = new User(id.getIdUser());
        business = new Business(id.getIdBusiness());
    }
}
