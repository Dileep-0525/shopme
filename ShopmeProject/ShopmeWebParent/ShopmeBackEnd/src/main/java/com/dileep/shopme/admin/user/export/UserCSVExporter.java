package com.dileep.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.dileep.shopme.admin.user.AbstractExporter;
import com.dileep.shopme.common.entity.User;

public class UserCSVExporter extends AbstractExporter{

	public void export(List<User> users , HttpServletResponse httpServletResponse) throws IOException {
		
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv");
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"User Id ", "E-Mail" , "First Name" , "Last Name" , "Roles" ,"Enabled"}; 
		String[] fieldMapping = {"id", "email" ,"firstName" ,"lastName","roles","enabled"};

		csvBeanWriter.writeHeader(csvHeader);
		for(User user:users) {
			csvBeanWriter.write(user, fieldMapping);
		}
		
		csvBeanWriter.close();
	}
	
}
