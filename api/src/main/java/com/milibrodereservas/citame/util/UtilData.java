package com.milibrodereservas.citame.util;

import jakarta.persistence.Column;

import java.lang.reflect.Field;

public class UtilData {
    public static Integer getFieldLength(Class<?> entityClass, String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                return columnAnnotation.length();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Campo no encontrado: " + fieldName, e);
        }
        return null; // No tiene @Column o no especifica length
    }
}
