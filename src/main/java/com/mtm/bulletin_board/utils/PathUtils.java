package com.mtm.bulletin_board.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class PathUtils extends HttpServletRequestWrapper {

    public PathUtils(HttpServletRequest request) {
        super(request);
    }

    public String getParam() {
        String pathInfo = super.getPathInfo();

        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                return pathParts[1];
            }
        }

        return null;
    }
}
