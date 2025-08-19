package com.milibrodereservas.citame.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Base {
    // Logger para las clases derivadas
    final protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static void throwException(String message, Exception e) {
        e.printStackTrace();
        throw new RuntimeException(message, e);
    }
}
