package com.milibrodereservas.citame.global;

import com.milibrodereservas.citame.entities.User;
import com.milibrodereservas.citame.model.*;
import com.milibrodereservas.citame.repositories.*;
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
        repoMap.put(AppointmentDto.class, ctx.getBean(AppointmentRepository.class));
        repoMap.put(BusinessDto.class, ctx.getBean(BusinessRepository.class));
        repoMap.put(MessageDto.class, ctx.getBean(MessageRepository.class));
        repoMap.put(ParameterDto.class, ctx.getBean(ParameterRepository.class));
        repoMap.put(ParameterServiceDto.class, ctx.getBean(ParameterServiceRepository.class));
        repoMap.put(ServiceDto.class, ctx.getBean(ServiceRepository.class));
        repoMap.put(TimetableDto.class, ctx.getBean(TimetableRepository.class));
        repoMap.put(UserDto.class, ctx.getBean(UserRepository.class));
    }

    public JpaRepository<?, ?> getRepoForEntity(Class<?> entityClass) {
        return repoMap.get(entityClass);
    }
}
