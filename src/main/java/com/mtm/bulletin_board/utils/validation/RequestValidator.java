package com.mtm.bulletin_board.utils.validation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mtm.bulletin_board.utils.NumberUtils;

public class RequestValidator {
    private final HttpServletRequest request;
    private final Map<String, ValidationSchema> schema;

    public RequestValidator(HttpServletRequest request) {
        this.request = request;
        this.schema = new HashMap<>();
    }

    public StringSchema buildStringSchema(String fieldName) {
        String value = request.getParameter(fieldName);

        return new StringSchema(value, fieldName);
    }

    public void addSchema(String fieldName, ValidationSchema schema) {

        if (schema instanceof FileSchema) {
            // Check Profile Image
            Part part;
            try {
                part = request.getPart(fieldName);
                schema.setValue(part);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }

        } else {
            String value = request.getParameter(fieldName);
            if (schema instanceof NumberSchema) {
                Integer valueInt = NumberUtils.parseInteger(value);
                schema.setValue(valueInt);
            }
            if (schema instanceof StringSchema) {
                schema.setValue(value);
            }
            if (schema instanceof DateSchema) {
                schema.setValue(value);
            }
        }
        schema.setFieldName(fieldName);

        this.schema.put(fieldName, schema);
    }

    public Map<String, ValidationSchema> getSchema() {
        return this.schema;
    }

    public Map<String, String> validate() {
        return new Validator(this.schema).validate();
    }

}
