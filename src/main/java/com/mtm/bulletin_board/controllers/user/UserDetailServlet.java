package com.mtm.bulletin_board.controllers.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.PathUtils;

@WebServlet("/users/profile/*")
public class UserDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		super.init();
		userService = new UserService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
					request.setAttribute("user", user);
					// Forward the request to the JSP page
					request.getRequestDispatcher("/views/user/profile.jsp").forward(request, response);
				} else {
					// User not found, send 404 response
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
				}
			} catch (NumberFormatException e) {
				// userIdString is not a valid integer, handle accordingly
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
			}
		} else {
			// userIdString is not exist
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
		}
	}
}
