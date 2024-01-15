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
import com.mtm.bulletin_board.utils.BCrypt;
import com.mtm.bulletin_board.utils.FileUtils;
import com.mtm.bulletin_board.utils.validation.*;

@WebServlet("/users/create")
@MultipartConfig
public class UserCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Gson gson;

	@Override
	public void init() throws ServletException {
		super.init();
		gson = new Gson();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Forward the request to the JSP page
		request.getRequestDispatcher("/views/user/createEdit.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> validate = validateRequest(request);

		if (validate == null || validate.isEmpty()) {

			try {

				// Store Image file
				FileUtils fileUtils = new FileUtils(getServletContext());
				Part profile = request.getPart("profile");
				String filePath = fileUtils.storeFile(profile);

				// Get User type
				UserType userType = UserType.fromCode(request.getParameter("type"));

				String hashPassword = BCrypt.hashPassword(request.getParameter("password"));
				// Set the attributes for the new user
				User newUser = new User();
				newUser.setName(request.getParameter("name"));
				newUser.setEmail(request.getParameter("email"));
				newUser.setPassword(hashPassword);
				newUser.setProfile(filePath);
				newUser.setType(userType);
				newUser.setPhone(request.getParameter("phone"));
				newUser.setAddress(request.getParameter("address"));
				newUser.setDobFromString(request.getParameter("dob"));

				request.setAttribute("user", newUser);

				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/confirm.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/createEdit.jsp");
				dispatcher.forward(request, response);
			}

		} else {
			String validationError = gson.toJson(validate);

			request.setAttribute("error", validationError);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/user/createEdit.jsp");
			dispatcher.forward(request, response);
		}
	}

	private Map<String, String> validateRequest(HttpServletRequest request) {
		RequestValidator reqValidator = new RequestValidator(request);

		reqValidator.addSchema("name", new StringSchema().required().min(5).max(50));
		reqValidator.addSchema("email", new StringSchema().required().email().max(100));
		reqValidator.addSchema("password", new StringSchema().required().min(5).max(100));
		reqValidator.addSchema("confirmPassword", new StringSchema().required().min(5).max(100));
		reqValidator.addSchema("type", new NumberSchema().required().min(1).max(2));
		reqValidator.addSchema("phone", new StringSchema().required().phone());
		reqValidator.addSchema("dob", new DateSchema().required().min("1900-01-01").maxToday());
		reqValidator.addSchema("address", new StringSchema().required().max(255));
		reqValidator.addSchema("profile", new FileSchema().required().image());

		Map<String, String> validation = reqValidator.validate();

		// Check Confirm Password Equal
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		if (!confirmPassword.equals(password)) {
			validation.put("confirmPassword", "Password and confirm password does not match.");
		}

		return validation;

	}
}
