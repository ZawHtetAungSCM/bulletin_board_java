package com.mtm.bulletin_board.utils;

import jakarta.servlet.http.HttpServletRequest;

public class FormUtils {

	private final HttpServletRequest request;

	public FormUtils(HttpServletRequest request) {
		this.request = request;
	}

	public String old(String fieldName) {
		String oldValue = request.getParameter(fieldName);
		if (oldValue != null) {
			return oldValue;
		}
		return "";
	}

	public String old(String fieldName, String value) {
		String oldValue = request.getParameter(fieldName);
		if (oldValue != null) {
			return oldValue;
		} else {
			return value;
		}
	}
}
