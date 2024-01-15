package com.mtm.bulletin_board.controllers.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.models.User.UserType;
import com.mtm.bulletin_board.services.AuthService;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.PathUtils;

@WebServlet("/users/confirm/*")
public class UserConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		super.init();
		userService = new UserService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Forward the request to the JSP page
		request.getRequestDispatcher("/views/user/confirm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String userIdString = pUtils.getParam();

		if (userIdString == null || userIdString.isEmpty()) {
			storeUser(request, response);
		} else {
			updateUser(request, response, userIdString);
		}
	}

	private void storeUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			AuthService authService = new AuthService(request);
			User loginUser = authService.getAuthUser();
			// Get User type
			UserType userType = UserType.fromCode(request.getParameter("type"));
			// Set the attributes for the new user
			User newUser = new User();
			newUser.setName(request.getParameter("name"));
			newUser.setEmail(request.getParameter("email"));
			newUser.setPassword(request.getParameter("password"));
			newUser.setProfile(request.getParameter("profile"));
			newUser.setType(userType);
			newUser.setPhone(request.getParameter("phone"));
			newUser.setAddress(request.getParameter("address"));
			newUser.setDobFromString(request.getParameter("dob"));
			newUser.setCreatedUserId(loginUser.getId());

			// Save User into databse
			User createdUser = userService.createUser(newUser);

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().append("User: Error");
		}

		response.sendRedirect(request.getContextPath() + "/users");
	}

	private void updateUser(HttpServletRequest request, HttpServletResponse response, String userIdString)
			throws IOException {

		try {
			// Try parsing userIdString to an integer
			int userId = Integer.parseInt(userIdString);

			// Get User data
			User user = userService.getById(userId);

			// Check if the user exists
			if (user != null) {
				AuthService authService = new AuthService(request);
				User loginUser = authService.getAuthUser();
				user.setName(request.getParameter("name"));
				user.setPhone(request.getParameter("phone"));
				user.setAddress(request.getParameter("address"));
				user.setDobFromString(request.getParameter("dob"));
				// TODO:: Remove Old Photo
				user.setProfile(request.getParameter("profile"));

				// Set User type
				String type = request.getParameter("type");
				if (type != null && !type.isEmpty()) {
					UserType userType = UserType.fromCode(type);
					if (userType != null) {
						user.setType(userType);
					}
				}
				user.setUpdatedUserId(loginUser.getId());

				// Update User into databse
				User updatedUser = userService.updateUser(user);

			} else {
				// User not found, send 404 response
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().append("User: Error");
		}

		response.sendRedirect(request.getContextPath() + "/users");
	}

}
