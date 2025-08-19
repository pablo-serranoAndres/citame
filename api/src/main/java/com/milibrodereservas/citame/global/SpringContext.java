package com.milibrodereservas.citame.global;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component // scope por defecto singleton
public class SpringContext implements ApplicationContextAware {
    // ApplicationContext hace que automaticamente se ejecute setApplicationContext para inyectar el contexto de la aplicacion
    // despues del constructor y despues que el contexto Spring esta inicializado

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
