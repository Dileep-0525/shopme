package com.dileep.shopme.admin.category.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.dileep.shopme.admin.user.AbstractExporter;
import com.dileep.shopme.common.entity.Category;

public class CategoryCSVExporter extends AbstractExporter {

	public void export(List<Category> categories, HttpServletResponse httpServletResponse) throws IOException {

		super.setResponseHeader(httpServletResponse, "text/csv", ".csv", "categories_" );

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Category Id ", "Category Name" , "Alias","Enabled"}; 
		String[] fieldMapping = {"id", "name" ,"alias" ,"enabled"};
		
		csvBeanWriter.writeHeader(csvHeader);
		for(Category category:categories) {
			
			if(category.getName().contains("--")) {
				category.setName(category.getName().replace("--", ""));
			}
			
			csvBeanWriter.write(category, fieldMapping);
		}
		
		csvBeanWriter.close();
	}
}
