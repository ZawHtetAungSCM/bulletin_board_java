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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> validate = validateRequest(request);

        if (validate == null || validate.isEmpty()) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Check email exist
            User user = userService.getByEmail(email);
            if (user == null) {
                Map<String, String> credentialError = new HashMap<>();
                credentialError.put("email", "Email is not register");
                String validationError = gson.toJson(credentialError);

                request.setAttribute("error", validationError);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/auth/login.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Check password match
            if (!BCrypt.checkPassword(password,  user.getPassword())) {
                Map<String, String> credentialError = new HashMap<>();
                credentialError.put("password", "Password is not correct");
                String validationError = gson.toJson(credentialError);

                request.setAttribute("error", validationError);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/auth/login.jsp");
                dispatcher.forward(request, response);
                return;
            }

            // Set user authentication status in session
            AuthService authService = new AuthService(request);
            authService.login(user);

            // Redirect to authorized page
            response.sendRedirect(request.getContextPath() + "/home");

        } else {
            String validationError = gson.toJson(validate);
            request.setAttribute("error", validationError);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/auth/login.jsp");
            dispatcher.forward(request, response);
        }
    }

    private Map<String, String> validateRequest(HttpServletRequest request) {
        RequestValidator reqValidator = new RequestValidator(request);

        reqValidator.addSchema("email", new StringSchema().required().email().max(100));
        reqValidator.addSchema("password", new StringSchema().required().min(5).max(100));

        return reqValidator.validate();
    }
}
