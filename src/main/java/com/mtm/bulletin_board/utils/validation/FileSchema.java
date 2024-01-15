package com.mtm.bulletin_board.utils.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import jakarta.servlet.http.Part;

public class FileSchema implements ValidationSchema<Part> {
	private Part part;
	private String fieldName;
	private final List<Supplier<String>> validators = new ArrayList<>();
	private boolean nullable = false;

	public FileSchema() {
	}

	public FileSchema(Part part, String fieldName) {
		setValue(part);
		setFieldName(fieldName);
	}

	public void setValue(Part part) {
		this.part = part;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public FileSchema required() {
		addMethod(() -> {
			String contentDisposition = part.getHeader("Content-Disposition");
			String filename = extractFileName(contentDisposition);
			if (part == null || filename == null || filename.isEmpty()) {
				return fieldName + " is a required field";
			}
			return null;
		});
		return this;
	}

	@Override
	public FileSchema nullable() {
		nullable = true;
		return this;
	}

	@Override
	public String validate() {
		if (fieldName == null || fieldName.isEmpty()) {
			return null;
		}
		String contentDisposition = part.getHeader("Content-Disposition");
		String filename = extractFileName(contentDisposition);
		for (Supplier<String> validator : validators) {
			if (nullable && (filename == null || filename.isEmpty())) {
				return null;
			}

			String error = validator.get();
			if (error != null) {
				return error;
			}

		}
		return null;
	}

	public FileSchema image() {
		addMethod(() -> {
			String contentType = part.getContentType();
			boolean isImage = contentType != null && contentType.startsWith("image");
			if (!isImage) {
				return fieldName + " is an invalid image file";
			} else {
				return null;
			}
		});
		return this;
	}

	private void addMethod(Supplier<String> func) {
		validators.add(func);
	}

	private String extractFileName(String contentDisposition) {
		int startIndex = contentDisposition.indexOf("filename=\"") + 10;
		int endIndex = contentDisposition.indexOf("\"", startIndex);
		return contentDisposition.substring(startIndex, endIndex);
	}
}
