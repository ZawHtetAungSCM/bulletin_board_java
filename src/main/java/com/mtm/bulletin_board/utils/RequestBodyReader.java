package com.mtm.bulletin_board.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestBodyReader {

    public static String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder jsonData = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = request.getReader();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return jsonData.toString();
    }
}
