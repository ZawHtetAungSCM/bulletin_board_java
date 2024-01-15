package com.mtm.bulletin_board.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BCrypt {

    public static String hashPassword(String password) {
        try {
            // Choose a hashing algorithm (e.g., SHA-256)
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hashed bytes
            byte[] hashedBytes = md.digest();

            // Convert hashed bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception appropriately
            return null;
        }
    }

    // Check if the entered password matches the stored hash
    public static boolean checkPassword(String enteredPassword, String storedHash) {
        String hashedEnteredPassword = hashPassword(enteredPassword);
        return hashedEnteredPassword != null && hashedEnteredPassword.equals(storedHash);
    }
}
