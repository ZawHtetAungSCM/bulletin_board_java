package com.mtm.bulletin_board.utils.validation;

public interface ValidationSchema<T> {

    public void setValue(T value);

    public void setFieldName(String fieldName);

    public ValidationSchema<T> required();

    public ValidationSchema<T> nullable();

    public String validate();
}
