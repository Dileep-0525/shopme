package com.dileep.shopme.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {

	public void setResponseHeader(HttpServletResponse httpServletResponse , String contentType , String extension, String entityName) throws IOException {

		DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

		String timeStamp = dateTimeFormatter.format(new Date());
		String fileName = entityName+ timeStamp + extension;
		httpServletResponse.setContentType(contentType);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		httpServletResponse.setHeader(headerKey, headerValue);

	}

}
