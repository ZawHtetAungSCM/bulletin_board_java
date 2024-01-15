package com.mtm.bulletin_board.utils;

public class NumberUtils {
    public static Integer parseInteger(String value) {

        try {
            Integer valueInt = (value != null && !value.isEmpty()) ? Integer.parseInt(value) : null;
            return valueInt;
        } catch (NumberFormatException e) {
            return null; // or throw an exception depending on your requirements
        }
    }
}
