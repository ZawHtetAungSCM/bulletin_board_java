package com.mtm.bulletin_board.controllers.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.AuthService;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.BCrypt;
import com.mtm.bulletin_board.utils.validation.RequestValidator;
import com.mtm.bulletin_board.utils.validation.StringSchema;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
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

        // Forward the request to the JSP page
        request.getRequestDispatcher("/views/auth/change-password.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> validate = validateRequest(request);

        if (validate == null || validate.isEmpty()) {

            AuthService authService = new AuthService(request);
            User loginUser = authService.getAuthUser();

            String oldPassword = request.getParameter("oldPassword");

            // Change Old Password is correct
            if (!BCrypt.checkPassword(oldPassword, loginUser.getPassword())) {
                Map<String, String> oldPasswordErr = new HashMap<>();
                oldPasswordErr.put("oldPassword", "Password is not correct");
                String validationError = gson.toJson(oldPasswordErr);

                request.setAttribute("error", validationError);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/auth/change-password.jsp");
                dispatcher.forward(request, response);
                return;
            }

            String hashPassword = BCrypt.hashPassword(request.getParameter("newPassword"));
            loginUser.setPassword(hashPassword);
            // Update User into databse
            userService.changePassword(loginUser);

            // Redirect to authorized page
            response.sendRedirect(request.getContextPath() + "/home");

        } else {
            String validationError = gson.toJson(validate);
            request.setAttribute("error", validationError);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/auth/change-password.jsp");
            dispatcher.forward(request, response);
        }
    }

    private Map<String, String> validateRequest(HttpServletRequest request) {
        RequestValidator reqValidator = new RequestValidator(request);

        reqValidator.addSchema("oldPassword", new StringSchema().required().min(5).max(100));
        reqValidator.addSchema("newPassword", new StringSchema().required().min(5).max(100));
        reqValidator.addSchema("confirmPassword", new StringSchema().required().min(5).max(100));

        Map<String, String> validation = reqValidator.validate();

        // Check Confirm Password Equal
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        if (!confirmPassword.equals(newPassword)) {
            validation.put("confirmPassword", "Password and confirm password does not match.");
        }

        return validation;
    }
}
