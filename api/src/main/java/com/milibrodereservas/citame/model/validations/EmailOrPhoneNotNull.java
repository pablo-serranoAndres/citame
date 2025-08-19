package com.milibrodereservas.citame.model.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailOrPhoneNotNullValidator.class)
@Documented
public @interface EmailOrPhoneNotNull {
    String message() default "Either email or phone must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
