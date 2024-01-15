package com.mtm.bulletin_board.controllers.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.AuthService;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.PathUtils;

@WebServlet("/users/delete/*")
public class UserDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		super.init();
		userService = new UserService();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String userIdString = pUtils.getParam();

		if (userIdString != null && !userIdString.isEmpty()) {
			try {
				// Try parsing userIdString to an integer
				int userId = Integer.parseInt(userIdString);
				// Get User data
				User user = userService.getById(userId);
				// Check if the user exists
				if (user != null) {
					AuthService authService = new AuthService(request);
					User loginUser = authService.getAuthUser();
					user.setDeletedUserId(loginUser.getId());
					// Update Data into database
					boolean isDeleted = userService.deleteUser(user);

				} else {
					// User not found, send 404 response
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().append("User Delete: Error");
			}

			response.sendRedirect(request.getContextPath() + "/users");

		} else {
			// userIdString is not exist
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
		}

	}

}
