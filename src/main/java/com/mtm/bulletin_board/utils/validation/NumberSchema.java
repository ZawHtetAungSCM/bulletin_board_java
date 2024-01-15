package com.mtm.bulletin_board.utils.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NumberSchema implements ValidationSchema<Integer> {
    private Integer value;
    private String fieldName;
    private final List<Supplier<String>> validators = new ArrayList<>();
    private boolean nullable = false;

    public NumberSchema() {
    }

    public NumberSchema(Integer value, String fieldName) {
        setValue(value);
        setFieldName(fieldName);
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public NumberSchema required() {
        addMethod(() -> {
            if (value == null) {
                return fieldName + " is a required field";
            }
            return null;
        });
        return this;
    }

    @Override
    public NumberSchema nullable() {
        nullable = true;
        return this;
    }

    public NumberSchema min(Integer minValue) {
        addMethod(() -> {
            if (value < minValue) {
                return fieldName + " must be at least " + minValue;
            }
            return null;
        });
        return this;
    }

    public NumberSchema max(Integer maxValue) {
        addMethod(() -> {
            if (value > maxValue) {
                return fieldName + " cannot exceed " + maxValue;
            }
            return null;
        });
        return this;
    }

    @Override
    public String validate() {
        if (fieldName == null || fieldName.isEmpty()) {
            return null;
        }
        for (Supplier<String> validator : validators) {
            if (nullable && value == null) {
                return null;
            }
            String error = validator.get();
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    private void addMethod(Supplier<String> func) {
        validators.add(func);
    }
}
