package com.nktin.spreadsheetreader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;




public class SpreadsheetReader {

	public SpreadsheetReader() {
		try
	    {
	        FileInputStream x = new FileInputStream(new File("C:\\Documents and Settings\\marchifl\\Desktop\\AlphaStreamNewsCalendar.xlsx"));

	        //Create Workbook instance holding reference to .xlsx file
	        Workbook wb = WorkbookFactory.create(x);
	        
	        //Get first/desired sheet from the workbook
	        Sheet sheet = wb.getSheetAt(0);

	        //Iterate through each rows one by one
	        for (Iterator<Row> iterator = sheet.iterator(); iterator.hasNext();) {
	            Row row = (Row) iterator.next();
	            //System.out.print(row.getCell(0).getCellType()+ " ");  
	            for (Iterator<Cell> iterator2 = row.iterator(); iterator2
	                    .hasNext();) {
	                Cell cell = (Cell) iterator2.next();
	                String value = null;
	                if(cell!=null) {
	                    switch(cell.getCellType()){
	                        case Cell.CELL_TYPE_BOOLEAN:
	                            value =String.valueOf(cell.getBooleanCellValue());
	                            break;
	                        case Cell.CELL_TYPE_NUMERIC:
	                        	 if(DateUtil.isCellDateFormatted(cell)) {
	                        		 value = new DataFormatter().formatCellValue(cell);
	                             } else {
	                            	 value = String.valueOf((int) (cell.getNumericCellValue()));
	                             }
	                            break;
	                        case Cell.CELL_TYPE_STRING:
	                            value=String.valueOf(cell.getStringCellValue());
	                            break;
	                        case Cell.CELL_TYPE_FORMULA:
	                            value=String.valueOf(cell.getCellFormula());
	                            break;
	                        case Cell.CELL_TYPE_BLANK:
	                            value="";
	                            break;
	                    }
	                    System.out.print(value);
	                    System.out.print(" ");
	                } else {
	                    System.out.println("Cell is null");
	                }
	            }      
	            System.out.println();
	        }         
	        x.close();
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public static void main(String[] args) {
		new SpreadsheetReader();
	}

}
