package com.mtm.bulletin_board.models;

import com.mtm.bulletin_board.models.User.UserType;

public class Auth {
    private User user;

    public Auth(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        if (user != null) {
            return user.getId();
        }
        return null;
    }

    public boolean isAdmin() {
        if (user != null) {
            return user.getType() == UserType.ADMIN;
        }
        return false;
    }
}
