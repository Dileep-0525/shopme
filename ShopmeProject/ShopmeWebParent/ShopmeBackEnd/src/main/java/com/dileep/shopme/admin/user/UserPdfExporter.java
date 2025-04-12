package com.dileep.shopme.admin.user;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.dileep.shopme.common.entity.User;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class UserPdfExporter extends AbstractExporter {

	public void export(List<User> users, HttpServletResponse httpServletResponse) throws IOException {
		super.setResponseHeader(httpServletResponse, "application/pdf", ".pdf");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, httpServletResponse.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of User", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(paragraph);
 		
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f,3.5f,3.0f,3.0f,3.0f,1.7f});
		
		
		writeTableHeader(table);
		writeTableData(table,users);

		document.add(table);
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<User> users) {

		for(User user:users) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstName());
			table.addCell(user.getLastName());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeader(PdfPTable table) {

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("Id" , font) );
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("E-Mail" , font) );
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("First Name" , font) );
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Last Name" , font) );
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Roles" , font) );
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled" , font) );
		table.addCell(cell);
	}

	
	
}
