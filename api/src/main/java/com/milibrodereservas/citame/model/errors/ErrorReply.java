package com.milibrodereservas.citame.model.errors;

import com.milibrodereservas.citame.services.ValidationException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorReply {
    private List<FieldErrorDetail> errors;

    public ErrorReply(Map<String, List<String>> errores) {
        errors = new ArrayList<>();
        for (String key : errores.keySet()) {
            FieldErrorDetail detail = new FieldErrorDetail();
            detail.setField(key);
            detail.setErrors(errores.get(key));
            errors.add(detail);
        }
    }

    public ErrorReply(ValidationException error) {
        errors = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        messages.add(error.getMessage());
        FieldErrorDetail detail = new FieldErrorDetail();
        detail.setField(error.getDetail());
        detail.setErrors(messages);
        errors.add(detail);
    }

}
