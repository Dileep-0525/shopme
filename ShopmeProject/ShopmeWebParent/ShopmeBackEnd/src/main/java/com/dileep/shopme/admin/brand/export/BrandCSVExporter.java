package com.dileep.shopme.admin.brand.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.dileep.shopme.admin.user.AbstractExporter;
import com.dileep.shopme.common.entity.Brand;

public class BrandCSVExporter extends AbstractExporter {

	public void export(List<Brand> brands, HttpServletResponse httpServletResponse) throws IOException {

		super.setResponseHeader(httpServletResponse, "text/csv", ".csv", "brands_" );

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"Brand Id ", "Brand Name" , "Categories"}; 
		String[] fieldMapping = {"id", "name" ,"categories"};
		
		csvBeanWriter.writeHeader(csvHeader);
		for(Brand brand:brands) {
			
			if(brand.getName().contains("--")) {
				brand.setName(brand.getName().replace("--", ""));
			}
			
			csvBeanWriter.write(brand, fieldMapping);
		}
		
		csvBeanWriter.close();
	}
}
