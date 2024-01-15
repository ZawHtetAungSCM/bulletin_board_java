package com.mtm.bulletin_board.filters;

import java.io.IOException;

import com.mtm.bulletin_board.models.Auth;
import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.AuthService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/*" }, filterName = "AuthFilter", description = "Filter all URLs except /login")
public class AuthFilter implements Filter {
    private String[] authRoutes = { "/login" };
    private String[] publicRoutes = { "/assets" };

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Check if the requested URL is excluded
        String requestURI = httpRequest.getRequestURI();

        boolean isPublicRoute = isPublicRoute(httpRequest.getContextPath(), requestURI);
        if(isPublicRoute) {
        	chain.doFilter(request, response);
        	return;
        }

        boolean isAuthRoute = isAuthRoute(httpRequest.getContextPath(), requestURI);

        AuthService authService = new AuthService(httpRequest);
        boolean isLoggedIn = authService.checkAuth();

        if(isLoggedIn){
            User loginUser = authService.getAuthUser();
            Auth authUser = new Auth(loginUser);
            httpRequest.setAttribute("auth", authUser);
        }

        if (isAuthRoute && isLoggedIn) {
            redirectToHomePage(httpResponse, httpRequest);
            return;
        }

        if (!isAuthRoute && !isLoggedIn) {
            redirectToLoginPage(httpResponse, httpRequest);
            return;
        }

        chain.doFilter(request, response);
    }

    private void redirectToLoginPage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        redirectPage(response, request, "/login");
    }

    private void redirectToHomePage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        redirectPage(response, request, "/home");
    }

    private void redirectPage(HttpServletResponse response, HttpServletRequest request, String url) throws IOException {
        response.sendRedirect(request.getContextPath() + url);
    }

    private boolean isAuthRoute(String contextPath, String requestURI) {
        for (String route : authRoutes) {
            if (requestURI.equals(contextPath + route)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isPublicRoute(String contextPath, String requestURI) {
        for (String route : publicRoutes) {
            if (requestURI.startsWith(contextPath + route)) {
                return true;
            }
        }
        return false;
    }
}
