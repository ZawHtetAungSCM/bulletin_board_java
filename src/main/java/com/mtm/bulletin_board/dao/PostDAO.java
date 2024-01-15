package com.mtm.bulletin_board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.models.Post.PostStatus;
import com.mtm.bulletin_board.utils.DbConnection;

public class PostDAO {

    public static Post getById(int postId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.getConnection();
            String query = "SELECT * FROM posts WHERE id = ? AND";
            query += softDelete();

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, postId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToPost(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return null;
    }

    public static List<Post> searchPosts(String keyword, int page, int limit) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Post> postList = new ArrayList<>();
        int offset = (page - 1) * limit;

        try {
            connection = DbConnection.getConnection();
            String query = "SELECT * FROM posts WHERE";
            query += getSearchFilterQuery(keyword);
            query += softDelete();
            query += " ORDER BY id DESC LIMIT ? OFFSET ?";

            preparedStatement = connection.prepareStatement(query);
            int parameterIndex = 1;
            parameterIndex = setSearchFilterParameters(preparedStatement, parameterIndex, keyword);
            preparedStatement.setInt(parameterIndex, limit);
            parameterIndex++;
            preparedStatement.setInt(parameterIndex, offset);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                postList.add(mapResultSetToPost(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return postList;
    }

    public static List<Post> getAllPostsForCsvDownload() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Post> postList = new ArrayList<>();

        try {
            connection = DbConnection.getConnection();
            String query = "SELECT * FROM posts WHERE";
            query += softDelete();

            preparedStatement = connection.prepareStatement(query);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                postList.add(mapResultSetToPost(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return postList;
    }

    public int savePostsFromCsvUpload(List<Post> posts) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try {
            connection = DbConnection.getConnection();
            String query = "INSERT INTO posts (title, description, status, created_user_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            for (Post post : posts) {
                preparedStatement.setString(1, post.getTitle());
                preparedStatement.setString(2, post.getDescription());
                preparedStatement.setInt(3, PostStatus.ACTIVE.getCode());
                preparedStatement.setInt(4, post.getCreatedUserId());
                preparedStatement.setTimestamp(5, currentTime);
                preparedStatement.addBatch();
            }

            int[] batchResults = preparedStatement.executeBatch();

            int successRow = 0;
            // Handle the batch results
            for (int i = 0; i < batchResults.length; i++) {
                if (batchResults[i] >= 0) {
                    successRow++;
                }
            }

            return successRow;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            DbConnection.closeResources(connection, preparedStatement, null);
        }
    }

    public int getTotalPostCountBySearch(String keyword) {
        int totalPostCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DbConnection.getConnection();
            String query = "SELECT COUNT(*) as total FROM posts WHERE";
            query += getSearchFilterQuery(keyword);
            query += softDelete();

            preparedStatement = connection.prepareStatement(query);
            int parameterIndex = 1;
            parameterIndex = setSearchFilterParameters(preparedStatement, parameterIndex, keyword);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalPostCount = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return totalPostCount;
    }

    public int save(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try {
            connection = DbConnection.getConnection();
            String query = "INSERT INTO posts (title, description, status, created_user_id, created_at) " +
                    "VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setInt(3, PostStatus.ACTIVE.getCode());
            preparedStatement.setInt(4, post.getCreatedUserId());
            preparedStatement.setTimestamp(5, currentTime);

            preparedStatement.executeUpdate();

            // Retrieve the generated keys
            generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                // Get the generated primary key
                return generatedKeys.getInt(1);
            } else {
                // Handle the case where no keys were generated
                return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            DbConnection.closeResources(connection, preparedStatement, null);
        }
    }

    public int update(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try {
            connection = DbConnection.getConnection();
            String query = "UPDATE posts SET title = ?, description = ?, status = ?, " +
                    "updated_user_id = ?, updated_at = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setInt(3, post.getStatus().getCode());
            preparedStatement.setInt(4, post.getUpdatedUserId());
            preparedStatement.setTimestamp(5, currentTime);
            preparedStatement.setInt(6, post.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            DbConnection.closeResources(connection, preparedStatement, null);
        }
    }

    public int delete(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        try {
            connection = DbConnection.getConnection();
            preparedStatement = connection.prepareStatement(
                    "UPDATE posts SET deleted_user_id = ?, deleted_at = ? WHERE id = ?");

            preparedStatement.setInt(1, post.getDeletedUserId());
            preparedStatement.setTimestamp(2, currentTime);
            preparedStatement.setInt(3, post.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            DbConnection.closeResources(connection, preparedStatement, null);
        }
    }

    private static Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        int status = resultSet.getInt("status");
        int createdUserId = resultSet.getInt("created_user_id");
        int updatedUserId = resultSet.getInt("updated_user_id");
        int deletedUserId = resultSet.getInt("deleted_user_id");
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        Timestamp deletedAt = resultSet.getTimestamp("deleted_at");

        // Map status string to PostStatus enum using the fromCode method
        Post.PostStatus postStatus = Post.PostStatus.fromCode(status);

        return new Post(id, title, description, postStatus, createdUserId, updatedUserId, deletedUserId, createdAt,
                updatedAt, deletedAt);
    }

    private static String softDelete() {
        return " deleted_at IS NULL";
    }

    private static String getSearchFilterQuery(String keyword) {
        String fQuery = "";
        if (keyword != null) {
            fQuery += " LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?) AND";
        }
        return fQuery;
    }

    private static int setSearchFilterParameters(PreparedStatement preparedStatement, int parameterIndex,
            String keyword) throws SQLException {
        if (keyword != null) {
            String searchPattern = "%" + keyword + "%";
            preparedStatement.setString(parameterIndex, searchPattern);
            parameterIndex++;
            preparedStatement.setString(parameterIndex, searchPattern);
            parameterIndex++;
        }
        return parameterIndex;
    }
}
