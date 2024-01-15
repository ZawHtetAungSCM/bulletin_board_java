package com.mtm.bulletin_board.services;

import java.util.Date;
import java.util.List;

import com.mtm.bulletin_board.dao.UserDAO;
import com.mtm.bulletin_board.models.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User getById(int id) {
        try {
            return userDAO.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getByEmail(String email) {
        try {
            return userDAO.getByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> searchUsers(int page, int limit, String keyword, Date startDate, Date endDate) {
        try {
            return userDAO.searchUsers(page, limit, keyword, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getTotalUserCountBySearch(String keyword, Date startDate, Date endDate) {
        try {
            return userDAO.getTotalUserCountBySearch(keyword, startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Integer> getAllUserIds() {
        try {
            return userDAO.getAllUserIds();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User createUser(User user) {
        int generatedUserId = userDAO.save(user);
        if (generatedUserId > 0) {
            return getById(generatedUserId);
        } else {
            return null;
        }
    }

    public User updateUser(User user) {
        int rowUpdated = userDAO.update(user);
        if (rowUpdated > 0) {
            return getById(user.getId());
        } else {
            return null;
        }
    }

    public boolean deleteUser(User user) {
        int rowUpdated = userDAO.delete(user);
        if (rowUpdated > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean changePassword(User user) {
        int rowUpdated = userDAO.changePassword(user);
        if (rowUpdated > 0) {
            return true;
        } else {
            return false;
        }
    }
}
