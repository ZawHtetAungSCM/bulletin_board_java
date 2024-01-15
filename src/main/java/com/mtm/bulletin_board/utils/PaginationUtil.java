package com.mtm.bulletin_board.utils;

import jakarta.servlet.http.HttpServletRequest;

public class PaginationUtil {
    public static void setPagination(HttpServletRequest request, int currentPage, int recordsPerPage,
            int totalItemCount) {
        int totalPages = (totalItemCount + recordsPerPage - 1) / recordsPerPage;

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalPages", totalPages);
    }

    public static int calculateStartIndex(int currentPage, int recordsPerPage) {
        return (currentPage - 1) * recordsPerPage;
    }
}
