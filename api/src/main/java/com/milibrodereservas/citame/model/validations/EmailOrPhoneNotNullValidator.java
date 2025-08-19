package com.milibrodereservas.citame.model.validations;

import com.milibrodereservas.citame.model.UserRegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class EmailOrPhoneNotNullValidator implements ConstraintValidator<EmailOrPhoneNotNull, UserRegisterRequest> {

    @Override
    public boolean isValid(UserRegisterRequest value, ConstraintValidatorContext context) {
        return value != null &&
                (StringUtils.isNotBlank(value.getEmail()) || StringUtils.isNotBlank(value.getPhone()));
    }
}
