package com.mtm.bulletin_board.utils.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class StringSchema implements ValidationSchema<String> {
    private String value;
    private String fieldName;
    private final List<Supplier<String>> validators = new ArrayList<>();
    private boolean nullable = false;

    public StringSchema() {
    }

    public StringSchema(String value, String fieldName) {
        setValue(value);
        setFieldName(fieldName);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public StringSchema required() {
        addMethod(() -> {
            if (value == null || value.isEmpty()) {
                return fieldName + " is a required field";
            }
            return null;
        });
        return this;
    }

    @Override
    public StringSchema nullable() {
        nullable = true;
        return this;
    }

    public StringSchema min(int minValue) {
        addMethod(() -> {
            if (value.length() < minValue) {
                return fieldName + " must be at least " + minValue + " characters";
            }
            return null;
        });
        return this;
    }

    public StringSchema max(int maxLength) {
        addMethod(() -> {
            if (value.length() > maxLength) {
                return fieldName + " cannot exceed " + maxLength + " characters";
            }
            return null;
        });
        return this;
    }

    public StringSchema email() {
        addMethod(() -> {
            Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
            if (!emailPattern.matcher(value).matches()) {
                return fieldName + " is an invalid email format";
            }
            return null;
        });
        return this;
    }

    public StringSchema phone() {
        addMethod(() -> {
            // Pattern phonePattern = Pattern.compile("^([0-9\\s\\-\\+\\(\\)]{8,15})$");
            // if (!phonePattern.matcher(value).matches()) {
            // return fieldName + " is an invalid phone format";
            // }
            if (!(value.length() > 5 && value.length() < 50)) {
                return fieldName + " is an invalid phone format";
            }
            return null;
        });
        return this;
    }

    public StringSchema url() {
        addMethod(() -> {
            Pattern urlPattern = Pattern.compile("^(https?|ftp):\\/\\/[^\\s/$.?#].[^\\s]*$");
            if (!urlPattern.matcher(value).matches()) {
                return fieldName + " is an invalid URL";
            }
            return null;
        });
        return this;
    }

    public StringSchema match(Pattern pattern) {
        addMethod(() -> {
            if (!pattern.matcher(value).matches()) {
                return fieldName + " is an invalid format";
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
