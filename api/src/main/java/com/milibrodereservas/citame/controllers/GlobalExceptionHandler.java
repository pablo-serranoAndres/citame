package com.milibrodereservas.citame.controllers;
import com.milibrodereservas.citame.model.errors.ErrorReply;
import com.milibrodereservas.citame.model.validations.EmailOrPhoneNotNull;
import com.milibrodereservas.citame.services.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja tu propia ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorReply> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(new ErrorReply(ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorReply> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errores = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            String fieldName = error.getField();
            String message = error.getDefaultMessage();
            List<String> detalles;
            if (errores.containsKey(fieldName)) {
                detalles = errores.get(fieldName);
            } else {
                detalles = new ArrayList<>();
            }
            detalles.add(message);
            errores.put(fieldName, detalles);
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            final String message = error.getDefaultMessage();
            final String dto = error.getObjectName();
            if ("userRegisterRequest".equalsIgnoreCase(dto)) {
                String field = "*";
                if ("email or phone are mandatory".equalsIgnoreCase(message)) {
                    field = "email/phone";
                }
                List<String> detalles;
                if (errores.containsKey(field)) {
                    detalles = errores.get(field);
                } else {
                    detalles = new ArrayList<>();
                }
                detalles.add(message);
                errores.put(field, detalles);
            }
        }

        ErrorReply errorReply = new ErrorReply(errores);
        return ResponseEntity.badRequest().body(errorReply);
    }

    // Otros errores genéricos (opcional)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorReply> handleOtherExceptions(Exception ex) {
        Map<String, List<String>> errores = new HashMap<>();
        List<String> error = new ArrayList<>();
        error.add("Internal server error: " + ex.getMessage());
        errores.put("*", error);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorReply(errores));
    }
}

