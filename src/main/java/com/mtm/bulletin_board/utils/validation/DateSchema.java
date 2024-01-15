package com.mtm.bulletin_board.utils.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class DateSchema implements ValidationSchema<String> {
    private Date value;
    private String fieldName;
    private final List<Supplier<String>> validators = new ArrayList<>();
    private boolean nullable = false;
    private final static String format = "yyyy-MM-dd";

    public DateSchema() {
    }

    public DateSchema(String dateString, String fieldName) {
        setValue(dateString);
        setFieldName(fieldName);
    }

    public void setValue(String dateString) {
        if (isValidDate(dateString)) {
            this.value = getDateFromString(dateString);
        }
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public DateSchema required() {
        addMethod(() -> {
            if (value == null) {
                return fieldName + " is a required field";
            }
            return null;
        });
        return this;
    }

    @Override
    public DateSchema nullable() {
        nullable = true;
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

    public DateSchema min(String dateString) {
        addMethod(() -> {
            Date minDate = getDateFromString(dateString);
            if (value != null && minDate != null) {
                if (!value.before(minDate)) {
                    return null;
                }
            }
            return fieldName + " must be after the minimum date " + dateString;
        });
        return this;
    }

    public DateSchema max(String dateString) {
        addMethod(() -> {
            Date maxDate = getDateFromString(dateString);
            if (value != null && maxDate != null) {
                if (!value.after(maxDate)) {
                    return null;
                }
            }
            return fieldName + " must be before the maximun date " + dateString;
        });
        return this;
    }

    public DateSchema maxToday() {
        addMethod(() -> {
            Date currentDate = new Date();
            if (value != null && currentDate != null) {
                if (!value.after(currentDate)) {
                    return null;
                }
            }
            return fieldName + " must be before Today";
        });
        return this;
    }

    private void addMethod(Supplier<String> func) {
        validators.add(func);
    }

    private static boolean isValidDate(String dateString) {
        Date parsedDate = getDateFromString(dateString);
        return parsedDate != null;
    }

    private static Date getDateFromString(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);

            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

}
