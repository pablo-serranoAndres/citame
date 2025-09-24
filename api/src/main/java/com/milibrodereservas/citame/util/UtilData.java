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

    // Valida el valor de un parámetro numérico
    public static boolean cumpleRango(String valorStr, String rangoStr, boolean esLong) {
        double valor;

        try {
            if (esLong) {
                // Verifica que no tenga parte decimal
                if (valorStr.contains(".") || valorStr.contains(",")) {
                    return false;
                }
                valor = Long.parseLong(valorStr);
            } else {
                valor = Double.parseDouble(valorStr);
            }
        } catch (NumberFormatException e) {
            return false;
        }

        rangoStr = rangoStr.trim();

        // Comparadores simples: < > <= >=
        if (rangoStr.matches("^[<>]=?\\s*-?\\d+(\\.\\d+)?$")) {
            return evalComparador(valor, rangoStr);
        }

        // Rango entre dos valores: [a,b] o (a,b)
        if (rangoStr.matches("^[\\[\\(]\\s*-?\\d+(\\.\\d+)?\\s*,\\s*-?\\d+(\\.\\d+)?\\s*[\\]\\)]$")) {
            return evalRango(valor, rangoStr);
        }

        return true; // Formato de rango no reconocido
    }
    private static boolean evalComparador(double valor, String expr) {
        expr = expr.replaceAll("\\s+", "");
        try {
            if (expr.startsWith(">=")) {
                return valor >= Double.parseDouble(expr.substring(2));
            } else if (expr.startsWith(">")) {
                return valor > Double.parseDouble(expr.substring(1));
            } else if (expr.startsWith("<=")) {
                return valor <= Double.parseDouble(expr.substring(2));
            } else if (expr.startsWith("<")) {
                return valor < Double.parseDouble(expr.substring(1));
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
    private static boolean evalRango(double valor, String expr) {
        boolean incluyeIzq = expr.startsWith("[");
        boolean incluyeDer = expr.endsWith("]");

        // Eliminar paréntesis y espacios
        String[] partes = expr.substring(1, expr.length() - 1).split(",");
        try {
            double inicio = Double.parseDouble(partes[0].trim());
            double fin = Double.parseDouble(partes[1].trim());

            boolean cumpleInicio = incluyeIzq ? valor >= inicio : valor > inicio;
            boolean cumpleFin = incluyeDer ? valor <= fin : valor < fin;

            return cumpleInicio && cumpleFin;
        } catch (NumberFormatException e) {
            return true;
        }
    }

}
