package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.AuthService;
import com.mtm.bulletin_board.services.PostService;
import com.mtm.bulletin_board.utils.PathUtils;

@WebServlet("/posts/delete/*")
public class PostDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PostService postService;

	@Override
	public void init() throws ServletException {
		super.init();
		postService = new PostService();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String postIdString = pUtils.getParam();

		if (postIdString != null && !postIdString.isEmpty()) {
			try {
				// Try parsing postIdString to an integer
				int postId = Integer.parseInt(postIdString);
				// Get Post data
				Post post = postService.getById(postId);
				// Check if the user exists
				if (post != null) {
					AuthService authService = new AuthService(request);
					User loginUser = authService.getAuthUser();
					post.setDeletedUserId(loginUser.getId());
					// Update Data into database
					boolean isDeleted = postService.deletePost(post);

				} else {
					// User not found, send 404 response
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().append("Post Delete: Error");
			}

			response.sendRedirect(request.getContextPath() + "/home");

		} else {
			// postIdString is not exist
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
		}

	}

}
