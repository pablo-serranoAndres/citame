package com.milibrodereservas.citame.global;

import com.milibrodereservas.citame.entities.User;
import com.milibrodereservas.citame.model.BusinessDto;
import com.milibrodereservas.citame.model.ServiceDto;
import com.milibrodereservas.citame.repositories.BusinessRepository;
import com.milibrodereservas.citame.repositories.ServiceRepository;
import com.milibrodereservas.citame.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RepositoryRegistry {
    // El scope por defecto de @Component es singleton: instancia unica
    // el ambito se cambia con @Scope prototype (nueva instancia cada vez que se solicita), request, session, application, websocket
    private final Map<Class<?>, JpaRepository<?, ?>> repoMap = new HashMap<>();

    @Autowired // no necesaria la anotacion al haber solo un constructor (desde Spring 4.3)
               // se puede anotar cualquier metodo y se llama despues del constructor al iniciar
    public RepositoryRegistry(ApplicationContext ctx) {
        // Registro de las relaciones entidad -> DAO manualmente
        repoMap.put(User.class, ctx.getBean(UserRepository.class));
        repoMap.put(BusinessDto.class, ctx.getBean(BusinessRepository.class));
        repoMap.put(ServiceDto.class, ctx.getBean(ServiceRepository.class));
    }

    public JpaRepository<?, ?> getRepoForEntity(Class<?> entityClass) {
        return repoMap.get(entityClass);
    }
}
