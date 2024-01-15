package com.mtm.bulletin_board.models;

import java.sql.Timestamp;

import com.mtm.bulletin_board.dao.UserDAO;

public class User {

    public enum UserType {
        ADMIN("1"),
        USER("2");

        private final String code;

        UserType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static UserType fromCode(String code) {
            for (UserType userType : UserType.values()) {
                if (userType.code.equals(code)) {
                    return userType;
                }
            }
            throw new IllegalArgumentException("Invalid UserType code: " + code);
        }
    }

    private int id;
    private String name;
    private String email;
    private String password;
    private String profile;
    private UserType type;
    private String phone;
    private String address;
    private java.sql.Date dob;
    private Integer createdUserId;
    private Integer updatedUserId;
    private Integer deletedUserId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public User() {
    }

    // Parameterized constructor
    public User(int id, String name, String email, String password, String profile, UserType type, String phone,
            String address, java.sql.Date dob, Integer createdUserId, Integer updatedUserId, Integer deletedUserId,
            Timestamp createdAt, Timestamp updatedAt, Timestamp deletedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.type = type;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.createdUserId = createdUserId;
        this.updatedUserId = updatedUserId;
        this.deletedUserId = deletedUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // Getter and Setter methods...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.sql.Date getDob() {
        return dob;
    }

    public void setDobFromString(String dobString) {
        this.dob = java.sql.Date.valueOf(dobString);
    }

    public void setDob(java.sql.Date dob) {
        this.dob = dob;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Integer getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(Integer updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public Integer getDeletedUserId() {
        return deletedUserId;
    }

    public void setDeletedUserId(Integer deletedUserId) {
        this.deletedUserId = deletedUserId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public User getCreatedUser() {
        if (this.createdUserId != null && this.createdUserId != 0) {
            return UserDAO.getById(this.createdUserId);
        }
        return null;
    }

    public User getUpdatedUser() {
        if (this.updatedUserId != null && this.updatedUserId != 0) {
            return UserDAO.getById(this.updatedUserId);
        }
        return null;
    }
}
