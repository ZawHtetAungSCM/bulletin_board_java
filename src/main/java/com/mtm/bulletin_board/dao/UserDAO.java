package com.mtm.bulletin_board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.utils.DbConnection;

public class UserDAO {

	public static User getById(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DbConnection.getConnection();
			String query = "SELECT * FROM users WHERE id = ? AND";
			query += softDelete();

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return mapResultSetToUser(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnection.closeResources(connection, preparedStatement, resultSet);
		}

		return null;
	}

	public static User getByEmail(String email) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DbConnection.getConnection();
			String query = "SELECT * FROM users WHERE email = ? AND";
			query += softDelete();

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return mapResultSetToUser(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnection.closeResources(connection, preparedStatement, resultSet);
		}

		return null;
	}

	public static List<User> searchUsers(int page, int limit, String keyword, Date startDate, Date endDate) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		List<User> userList = new ArrayList<>();
		int offset = (page - 1) * limit;

		try {
			connection = DbConnection.getConnection();
			String query = "SELECT * FROM users WHERE";
			query += getSearchFilterQuery(keyword, startDate, endDate);
			query += softDelete();
			query += " ORDER BY id DESC LIMIT ? OFFSET ?";

			preparedStatement = connection.prepareStatement(query);
			int parameterIndex = 1;

			parameterIndex = setSearchFilterParameters(preparedStatement,parameterIndex,keyword,startDate,endDate);

			preparedStatement.setInt(parameterIndex, limit);
			parameterIndex++;
			preparedStatement.setInt(parameterIndex, offset);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				userList.add(mapResultSetToUser(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnection.closeResources(connection, preparedStatement, resultSet);
		}

		return userList;
	}

	public int getTotalUserCountBySearch(String keyword, Date startDate, Date endDate) {
		int totalUserCount = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DbConnection.getConnection();
			String query = "SELECT COUNT(*) as total FROM users WHERE";
			query += getSearchFilterQuery(keyword, startDate, endDate);
			query += softDelete();

			preparedStatement = connection.prepareStatement(query);
			int parameterIndex = 1;

			parameterIndex = setSearchFilterParameters(preparedStatement,parameterIndex,keyword,startDate,endDate);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				totalUserCount = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnection.closeResources(connection, preparedStatement, resultSet);
		}

		return totalUserCount;
	}

	public List<Integer> getAllUserIds() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Integer> userIds = new ArrayList<>();

		try {
			connection = DbConnection.getConnection();
			String query = "SELECT id FROM users WHERE";
			query += softDelete();

			preparedStatement = connection.prepareStatement(query);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int userId = resultSet.getInt("id");
				userIds.add(userId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbConnection.closeResources(connection, preparedStatement, resultSet);
		}

		return userIds;
	}

	public int save(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		try {
			connection = DbConnection.getConnection();
			String query = "INSERT INTO users (name, email, password, profile, type, phone, address, dob, "
					+ "created_user_id, created_at) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getProfile());
			preparedStatement.setString(5, user.getType().getCode());
			preparedStatement.setString(6, user.getPhone());
			preparedStatement.setString(7, user.getAddress());
			preparedStatement.setDate(8, user.getDob());
			preparedStatement.setInt(9, user.getCreatedUserId());
			preparedStatement.setTimestamp(10, currentTime);

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

	public int update(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		try {
			connection = DbConnection.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE users SET name = ?, email = ?, profile = ?, type = ?, "
							+ "phone = ?, address = ?, dob = ?, updated_user_id = ?, updated_at = ? "
							+ "WHERE id = ?");

			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getProfile());
			preparedStatement.setString(4, user.getType().getCode());
			preparedStatement.setString(5, user.getPhone());
			preparedStatement.setString(6, user.getAddress());
			preparedStatement.setDate(7, user.getDob());
			preparedStatement.setInt(8, user.getUpdatedUserId());
			preparedStatement.setTimestamp(9, currentTime);
			preparedStatement.setInt(10, user.getId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			DbConnection.closeResources(connection, preparedStatement, null);
		}
	}

	public int delete(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		try {
			connection = DbConnection.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE users SET deleted_user_id = ?, deleted_at = ? WHERE id = ?");

			preparedStatement.setInt(1, user.getDeletedUserId());
			preparedStatement.setTimestamp(2, currentTime);
			preparedStatement.setInt(3, user.getId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			DbConnection.closeResources(connection, preparedStatement, null);
		}
	}

	public int changePassword(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DbConnection.getConnection();
			preparedStatement = connection.prepareStatement(
					"UPDATE users SET password = ? WHERE id = ?");

			preparedStatement.setString(1, user.getPassword());
			preparedStatement.setInt(2, user.getId());

			int rowsUpdated = preparedStatement.executeUpdate();

			return rowsUpdated;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			DbConnection.closeResources(connection, preparedStatement, null);
		}
	}


	private static User mapResultSetToUser(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("id");
		String name = resultSet.getString("name");
		String email = resultSet.getString("email");
		String password = resultSet.getString("password");
		String profile = resultSet.getString("profile");
		User.UserType type = User.UserType.fromCode(resultSet.getString("type"));
		String phone = resultSet.getString("phone");
		String address = resultSet.getString("address");
		java.sql.Date dob = resultSet.getDate("dob");
		int createdUserId = resultSet.getInt("created_user_id");
		int updatedUserId = resultSet.getInt("updated_user_id");
		int deletedUserId = resultSet.getInt("deleted_user_id");
		Timestamp createdAt = resultSet.getTimestamp("created_at");
		Timestamp updatedAt = resultSet.getTimestamp("updated_at");
		Timestamp deletedAt = resultSet.getTimestamp("deleted_at");

		return new User(id, name, email, password, profile, type, phone, address, dob, createdUserId, updatedUserId,
				deletedUserId, createdAt, updatedAt, deletedAt);
	}

	private static String softDelete() {
		return " deleted_at IS NULL";
	}

	private static String getSearchFilterQuery(String keyword, Date startDate, Date endDate) {
		String fQuery = "";
		if (keyword != null) {
			fQuery += " LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) AND";
		}

		if (startDate != null) {
			fQuery += " created_at >= ? AND";
		}

		if (endDate != null) {
			fQuery += " created_at <= ? AND";
		}
		return fQuery;
	}

	private static int setSearchFilterParameters(PreparedStatement preparedStatement, int parameterIndex, String keyword, Date startDate, Date endDate) throws SQLException {
        if (keyword != null) {
			String searchPattern = "%" + keyword + "%";
            preparedStatement.setString(parameterIndex, searchPattern);
            parameterIndex++;
            preparedStatement.setString(parameterIndex, searchPattern);
            parameterIndex++;
        }
		if (startDate != null) {
			preparedStatement.setDate(parameterIndex, new java.sql.Date(startDate.getTime()));
			parameterIndex++;
		}

		if (endDate != null) {
			preparedStatement.setDate(parameterIndex, new java.sql.Date(endDate.getTime()));
			parameterIndex++;
		}
		return parameterIndex;
    }
}
