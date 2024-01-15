package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.services.PostService;
import com.mtm.bulletin_board.utils.PaginationUtil;

@WebServlet(name = "PostList", urlPatterns = { "/home", "/posts" })
public class PostListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PostService postService;

	@Override
	public void init() throws ServletException {
		super.init();
		postService = new PostService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int currentPage = 1;
		int recordsPerPage = 9;
		String reqKw = request.getParameter("kw");
		String searchKeyword = null;
		if (reqKw != null && !reqKw.isEmpty()) {
			searchKeyword = reqKw;
		}
		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}

		List<Post> postList = postService.searchPosts(searchKeyword, currentPage, recordsPerPage);
		int totalPostCount = postService.getTotalPostCountBySearch(searchKeyword);

		PaginationUtil.setPagination(request, currentPage, recordsPerPage, totalPostCount);

		request.setAttribute("postList", postList);
		request.setAttribute("searchKeyword", searchKeyword);

		// Forward to the user list page
		RequestDispatcher dispatcher = request.getRequestDispatcher("views/post/list.jsp");
		dispatcher.forward(request, response);
	}
}
