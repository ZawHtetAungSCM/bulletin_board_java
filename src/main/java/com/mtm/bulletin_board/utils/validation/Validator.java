package com.mtm.bulletin_board.utils.validation;

import java.util.HashMap;
import java.util.Map;

public class Validator {

    private final Map<String, ValidationSchema> schema;

    public Validator(Map<String, ValidationSchema> schema) {
        this.schema = schema;
    }

    public Map<String, String> validate() {
        Map<String, String> validationErrors = new HashMap<>();

        for (Map.Entry<String, ValidationSchema> entry : schema.entrySet()) {
            String key = entry.getKey();
            ValidationSchema value = entry.getValue();
            String error = value.validate();
            if (error != null) {
                validationErrors.put(key, error);
            }
        }

        return validationErrors;
    }
}
