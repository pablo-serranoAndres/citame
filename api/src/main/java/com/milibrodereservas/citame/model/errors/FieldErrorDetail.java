package com.milibrodereservas.citame.model.errors;

import lombok.Data;
import java.util.List;

@Data
public class FieldErrorDetail {
    private String field;
    private List<String> errors;
}
