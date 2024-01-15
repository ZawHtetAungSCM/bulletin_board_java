package com.mtm.bulletin_board.controllers.post;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.mtm.bulletin_board.models.Post;
import com.mtm.bulletin_board.services.PostService;

@WebServlet("/posts/download-csv")
public class PostCsvDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PostService postService;

	@Override
	public void init() throws ServletException {
		super.init();
		postService = new PostService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Post> postList = postService.getAllPostsForCsvDownload();

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=bulletin_board.csv");

		try (PrintWriter writer = response.getWriter()) {
			// Write CSV header
			writer.println("Title,Description,Created User Id");

			// Write CSV data
			for (Post post : postList) {
				writer.println(post.getTitle() + "," + post.getDescription() + "," + post.getCreatedUserId());
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error writing CSV data");
		}
	}
}
