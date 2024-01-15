package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.models.Post.PostStatus;
import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.AuthService;
import com.mtm.bulletin_board.services.PostService;
import com.mtm.bulletin_board.utils.PathUtils;

@WebServlet("/posts/confirm/*")
public class PostConfirmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PostService postService;

	@Override
	public void init() throws ServletException {
		super.init();
		postService = new PostService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Forward the request to the JSP page
		request.getRequestDispatcher("/views/post/confirm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PathUtils pUtils = new PathUtils(request);
		String postIdString = pUtils.getParam();

		if (postIdString == null || postIdString.isEmpty()) {
			storePost(request, response);
		} else {
			updatePost(request, response, postIdString);
		}
	}

	private void storePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			AuthService authService = new AuthService(request);
			User loginUser = authService.getAuthUser();
			// Set the attributes for the new post
			Post newPost = new Post();
			newPost.setTitle(request.getParameter("title"));
			newPost.setDescription(request.getParameter("description"));
			newPost.setCreatedUserId(loginUser.getId());

			// Save Post into databse
			postService.createPost(newPost);

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().append("Post Store: Error");
		}

		response.sendRedirect(request.getContextPath() + "/home");
	}

	private void updatePost(HttpServletRequest request, HttpServletResponse response, String userIdString)
			throws IOException {

		try {
			// Try parsing postIdString to an integer
			int postId = Integer.parseInt(userIdString);

			// Get Post data
			Post post = postService.getById(postId);

			// Check if the post exists
			if (post != null) {
				AuthService authService = new AuthService(request);
				User loginUser = authService.getAuthUser();
				post.setTitle(request.getParameter("title"));
				post.setDescription(request.getParameter("description"));

				// Set Post Status
				String status = request.getParameter("status");
				int statusInt = status != null && !status.isEmpty() ? 1 : 0;
				PostStatus postStatus = PostStatus.fromCode(statusInt);
				if (postStatus != null) {
					post.setStatus(postStatus);
				}

				post.setUpdatedUserId(loginUser.getId());

				// Update Post into databse
				postService.updatePost(post);

			} else {
				// Post not found, send 404 response
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().append("Post Update: Error");
		}

		response.sendRedirect(request.getContextPath() + "/home");
	}

}
