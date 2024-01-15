package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.models.Post.PostStatus;
import com.mtm.bulletin_board.services.PostService;
import com.mtm.bulletin_board.utils.PathUtils;
import com.mtm.bulletin_board.utils.validation.RequestValidator;
import com.mtm.bulletin_board.utils.validation.StringSchema;

@WebServlet("/posts/edit/*")
@MultipartConfig
public class PostEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PostService postService;
	private Gson gson;

	@Override
	public void init() throws ServletException {
		super.init();
		postService = new PostService();
		gson = new Gson();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String postIdString = pUtils.getParam();

		if (postIdString != null && !postIdString.isEmpty()) {
			try {
				// Try parsing post to an integer
				int postId = Integer.parseInt(postIdString);

				// Get Post data
				Post post = postService.getById(postId);

				// Check if the post exists
				if (post != null) {
					request.setAttribute("isEdit", true);
					request.setAttribute("post", post);
					// Forward the request to the JSP page
					request.getRequestDispatcher("/views/post/createEdit.jsp").forward(request, response);
				} else {
					// Post not found, send 404 response
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
				}
			} catch (NumberFormatException e) {
				// postIdString is not a valid integer, handle accordingly
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
			}
		} else {
			// postIdString is not exist
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String postIdString = pUtils.getParam();
		Post post;

		if (postIdString == null || postIdString.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Post Id");
			return;
		}

		try {
			// Try parsing postIdString to an integer
			int postId = Integer.parseInt(postIdString);

			// Get Post data
			post = postService.getById(postId);

			// Check if the user exists
			if (post == null) {
				// User not found, send 404 response
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Post Id");
			return;
		}

		request.setAttribute("isEdit", true);

		Map<String, String> validate = validateRequest(request);

		if (validate == null || validate.isEmpty()) {
			try {

				post.setTitle(request.getParameter("title"));
				post.setDescription(request.getParameter("description"));

				// Set Post Status
				String status = request.getParameter("status");
				int statusInt = status != null && !status.isEmpty() ? 1 : 0;
				PostStatus postStatus = PostStatus.fromCode(statusInt);
				if (postStatus != null) {
					post.setStatus(postStatus);
				}

				request.setAttribute("post", post);

				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/post/confirm.jsp");
				dispatcher.forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				// TODO:: Display Error Alert
				request.setAttribute("post", post);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/views/post/createEdit.jsp");
				dispatcher.forward(request, response);
			}

		} else {
			String validationError = gson.toJson(validate);

			request.setAttribute("post", post);
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
