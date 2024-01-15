package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.utils.validation.*;

@WebServlet("/posts/create")
public class PostCreateServlet extends HttpServlet {
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
		request.getRequestDispatcher("/views/post/createEdit.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> validate = validateRequest(request);

		if (validate == null || validate.isEmpty()) {
			try {
				Post newPost = new Post();
				newPost.setTitle(request.getParameter("title"));
				newPost.setDescription(request.getParameter("description"));

				request.setAttribute("post", newPost);

				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/post/confirm.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/post/createEdit.jsp");
				dispatcher.forward(request, response);
			}

		} else {
			String validationError = gson.toJson(validate);

			request.setAttribute("error", validationError);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/views/post/createEdit.jsp");
			dispatcher.forward(request, response);
		}
	}

	private Map<String, String> validateRequest(HttpServletRequest request) {
		RequestValidator reqValidator = new RequestValidator(request);

		reqValidator.addSchema("title", new StringSchema().required().min(5).max(255));
		reqValidator.addSchema("description", new StringSchema().required().max(255));

		return reqValidator.validate();
	}
}
