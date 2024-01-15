package com.mtm.bulletin_board.services;

import java.security.SecureRandom;
import java.util.Base64;

import com.mtm.bulletin_board.dao.UserDAO;
import com.mtm.bulletin_board.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AuthService {

    private UserDAO userDAO;
    private HttpServletRequest request;
    private String SESSION_KEY = "auth-user-id";

    public AuthService(HttpServletRequest request) {
        this.request = request;
        this.userDAO = new UserDAO();
    }

    public void login(User user) {
        HttpSession session = request.getSession();

        // TODO:: store token in database or file
        // String token = generateToken();
        setAuthSession(session, user.getId());
    }

    public void logout() {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public boolean checkAuth() {
        HttpSession session = request.getSession(false);
        int sessionVal = getAuthSession(session);

        // TODO:: check token from database or file
        // return sessionVal != null && !sessionVal.isEmpty();
        return sessionVal != 0;
    }

    public User getAuthUser() {
        HttpSession session = request.getSession(false);
        int sessionVal = getAuthSession(session);

        if (sessionVal == 0) {
            return null;
        } else {
            User user = userDAO.getById(sessionVal);

            return user;
        }
    }

    private void setAuthSession(HttpSession session, int value) {
        session.setAttribute(SESSION_KEY, value);
    }

    private int getAuthSession(HttpSession session) {
        Integer value = (session != null) ? (Integer) session.getAttribute(SESSION_KEY) : null;
        return (value != null) ? value.intValue() : 0;
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}