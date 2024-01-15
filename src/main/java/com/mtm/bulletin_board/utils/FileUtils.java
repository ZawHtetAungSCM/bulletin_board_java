package com.mtm.bulletin_board.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

public class FileUtils {

	private final ServletContext servletContext;
	private final String UPLOAD_DIRECTORY = "image";

	public FileUtils(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String storeFile(Part part) {
		try {
			// Upload Directory
			String uploadPath = this.servletContext.getRealPath("") + UPLOAD_DIRECTORY;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			// Generate a timestamp
			String originalFilename = part.getSubmittedFileName();
			String fileExt = getFileExtension(originalFilename);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String timestamp = dateFormat.format(new Date());
			String randomFilename = UUID.randomUUID().toString();
			String fileName = timestamp + "_" + randomFilename + "." + fileExt;

			// String fileName = getFileName(part);
			String storedPath = uploadPath + File.separator + fileName;
			part.write(storedPath);

			String relativeStorePath = File.separator + UPLOAD_DIRECTORY + File.separator + fileName;

			return relativeStorePath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getFileExtension(String filename) {
		if (filename == null || filename.isEmpty()) {
			return "";
		}

		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			// No dot (.) in the filename
			return "";
		}

		// Extract and return the substring after the last dot
		return filename.substring(lastDotIndex + 1);
	}

	public static String getFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		String[] tokens = contentDisposition.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return "";
	}
}
