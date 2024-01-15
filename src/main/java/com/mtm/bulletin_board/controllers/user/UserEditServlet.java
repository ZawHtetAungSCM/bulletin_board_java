package com.mtm.bulletin_board.controllers.user;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.models.User.UserType;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.FileUtils;
import com.mtm.bulletin_board.utils.PathUtils;
import com.mtm.bulletin_board.utils.validation.DateSchema;
import com.mtm.bulletin_board.utils.validation.FileSchema;
import com.mtm.bulletin_board.utils.validation.NumberSchema;
import com.mtm.bulletin_board.utils.validation.RequestValidator;
import com.mtm.bulletin_board.utils.validation.StringSchema;

@WebServlet("/users/edit/*")
@MultipartConfig
public class UserEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;
	private Gson gson;

	@Override
	public void init() throws ServletException {
		super.init();
		userService = new UserService();
		gson = new Gson();
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
					request.setAttribute("isEdit", true);
					request.setAttribute("user", user);
					// Forward the request to the JSP page
					request.getRequestDispatcher("/views/user/createEdit.jsp").forward(request, response);
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String userIdString = pUtils.getParam();
		User user;

		if (userIdString == null || userIdString.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User Id");
			return;
		}

		try {
			// Try parsing userIdString to an integer
			int userId = Integer.parseInt(userIdString);

			// Get User data
			user = userService.getById(userId);

			// Check if the user exists
			if (user == null) {
				// User not found, send 404 response
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User Id");
			return;
		}

		request.setAttribute("isEdit", true);

		Map<String, String> validate = validateRequest(request);

		if (validate == null || validate.isEmpty()) {

			try {
				user.setName(request.getParameter("name"));
				user.setPhone(request.getParameter("phone"));
				user.setAddress(request.getParameter("address"));
				user.setDobFromString(request.getParameter("dob"));

				// Set User type
				String type = request.getParameter("type");
				if (type != null && !type.isEmpty()) {
					UserType userType = UserType.fromCode(type);
					if (userType != null) {
						user.setType(userType);
					}
				}

				// Store Image file
				FileUtils fileUtils = new FileUtils(getServletContext());
				Part profile = request.getPart("profile");

				if (profile != null) {
					String filePath = fileUtils.storeFile(profile);
					user.setProfile(filePath);
				}

				request.setAttribute("user", user);

				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/confirm.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				// TODO:: Display Error Alert
				request.setAttribute("user", user);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/createEdit.jsp");
				dispatcher.forward(request, response);
			}

		} else {
			String validationError = gson.toJson(validate);

			request.setAttribute("error", validationError);
			request.setAttribute("user", user);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/createEdit.jsp");
			dispatcher.forward(request, response);
		}

	}

	private Map<String, String> validateRequest(HttpServletRequest request) {
		RequestValidator reqValidator = new RequestValidator(request);

		reqValidator.addSchema("name", new StringSchema().required().min(5).max(50));
		reqValidator.addSchema("type", new NumberSchema().nullable().min(1).max(2));
		reqValidator.addSchema("phone", new StringSchema().required().phone());
		reqValidator.addSchema("dob", new DateSchema().required().min("1900-01-01").maxToday());
		reqValidator.addSchema("address", new StringSchema().required().max(255));
		reqValidator.addSchema("profile", new FileSchema().nullable().image());

		return reqValidator.validate();
	}

}
