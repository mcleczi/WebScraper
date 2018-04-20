package display;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
	
	Workbook HeaderWorkbook;
	Sheet HeaderWorksheet;
	FileOutputStream fileOut;
	
	public Excel(){
		HeaderWorkbook = null;
		HeaderWorksheet = null;
		fileOut = null;
	}
	
	
	
	
	public void createHeader(String filename, String sheetname){
		try {
			fileOut = new FileOutputStream(filename);
			HeaderWorkbook = new XSSFWorkbook();
			

			
			HeaderWorksheet = HeaderWorkbook.createSheet(sheetname);
			Sheet statesheet = HeaderWorkbook.createSheet("Sheet2");
		
			Row titleRow = HeaderWorksheet.createRow(0);
			
			Cell cellA0 = titleRow.createCell( 0);
			cellA0.setCellValue("Title");
			
			Cell cellB0 = titleRow.createCell(1);
			cellB0.setCellValue("Company");
			
			Cell cellC0 = titleRow.createCell(2);
			cellC0.setCellValue("Location");
			
			Cell cellD0 = titleRow.createCell(3);
			cellD0.setCellValue("Min Salary");
			
			Cell cellE0 = titleRow.createCell(4);
			cellE0.setCellValue("Avg Salary");
			
			Cell cellF0 = titleRow.createCell(5);
			cellF0.setCellValue("Max Salary");
			
			Cell cellG0 = titleRow.createCell(6);
			cellG0.setCellValue("URL");

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public void AddRow(Workbook workbook, Sheet worksheet, int index, String title, String company, 
								String loc, String minSal, String avgSal, String maxSal, String fullPostUrl){
		
		Row row = worksheet.createRow(index);
		
		Cell cellA1 = row.createCell(0);
		cellA1.setCellValue(title);
		
		Cell cellB1 = row.createCell(1);
		cellB1.setCellValue(company);
		
		Cell cellC1 = row.createCell(2);
		cellC1.setCellValue(loc);
		
		Cell cellD1 = row.createCell(3);
		cellD1.setCellValue(minSal);
		
		Cell cellE1 = row.createCell(4);
		cellE1.setCellValue(avgSal); 
		
		Cell cellF1 = row.createCell(5);
		cellF1.setCellValue(maxSal);
		
		Cell cellG1 = row.createCell(6);
		cellG1.setCellValue(fullPostUrl);

	}
	
	public void WriteAndClose(){
		try{
			HeaderWorkbook.write(fileOut);
			fileOut.close();
			HeaderWorkbook.close();			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Workbook getWorkbook(){
		return HeaderWorkbook;
	}
	public Sheet getSheet(){
		return HeaderWorksheet;
	}
	
}
