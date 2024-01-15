package com.mtm.bulletin_board.controllers.user;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.mtm.bulletin_board.models.User;
import com.mtm.bulletin_board.services.UserService;
import com.mtm.bulletin_board.utils.DateUtils;
import com.mtm.bulletin_board.utils.PaginationUtil;

@WebServlet("/users")
public class UserListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	@Override
	public void init() throws ServletException {
		super.init();
		userService = new UserService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int currentPage = 1;
		int recordsPerPage = 10;
		String reqKw = request.getParameter("kw");
		String reqDate = request.getParameter("date");
		String searchKeyword = null;
		Date startDate = null;
		Date endDate = null;
		if (reqKw != null && !reqKw.isEmpty()) {
			searchKeyword= reqKw;
		}
		if (reqDate != null && !reqDate.isEmpty()) {
			String[] dateArr = reqDate.split("-");
			String sD = dateArr.length > 0  ? dateArr[0] : null;
			String eD = dateArr.length > 1  ? dateArr[1] : null;

			startDate = DateUtils.parseDate(sD);
			endDate = DateUtils.parseDate(eD);
		}

		if (request.getParameter("page") != null) {
			currentPage = Integer.parseInt(request.getParameter("page"));
		}


		List<User> userList= userService.searchUsers(currentPage, recordsPerPage, searchKeyword, startDate, endDate);
		int totalUserCount = userService.getTotalUserCountBySearch(searchKeyword, startDate, endDate);

		PaginationUtil.setPagination(request, currentPage, recordsPerPage, totalUserCount);

		request.setAttribute("userList", userList);
		request.setAttribute("searchKeyword", reqKw);
		request.setAttribute("searchDate", reqDate);

		// Forward to the user list page
		RequestDispatcher dispatcher = request.getRequestDispatcher("views/user/list.jsp");
		dispatcher.forward(request, response);
	}

}
