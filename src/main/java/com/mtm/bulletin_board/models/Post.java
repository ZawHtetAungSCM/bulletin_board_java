package com.mtm.bulletin_board.models;

import java.sql.Timestamp;

import com.mtm.bulletin_board.dao.UserDAO;

public class Post {

    public enum PostStatus {
        INACTIVE(0),
        ACTIVE(1);

        private final int code;

        PostStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static PostStatus fromCode(int code) {
            for (PostStatus postStatus : PostStatus.values()) {
                if (postStatus.code == code) {
                    return postStatus;
                }
            }
            throw new IllegalArgumentException("Invalid Post Status code: " + code);
        }
    }

    private int id;
    private String title;
    private String description;
    private PostStatus status;
    private Integer createdUserId;
    private Integer updatedUserId;
    private Integer deletedUserId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public Post() {
    }

    public Post(int id, String title, String description, PostStatus status, Integer createdUserId,
            Integer updatedUserId, Integer deletedUserId, Timestamp createdAt, Timestamp updatedAt,
            Timestamp deletedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdUserId = createdUserId;
        this.updatedUserId = updatedUserId;
        this.deletedUserId = deletedUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
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

    public boolean isPublic() {
        if (status != null) {
            return status == PostStatus.ACTIVE;
        }
        return false;
    }
}
