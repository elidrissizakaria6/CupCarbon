package perso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import project.Project;

public class Excel  extends Thread {
	DecimalFormat df = new DecimalFormat("0");
	private XSSFWorkbook myWorkBook;
	public void run() {
//		writeFile();
		readfile();
	}
	public static void main(String args[]){
		Excel excel=new Excel();
		excel.start();
	}
	public void readfile(){
	 	try {
//	 		File myFile = new File("C://Users/Zakaria/Documents/Classeur"+Math.random()*20+".xlsx");
	 		File myFile = new File(Project.projectPath+"/Classeur.xlsx");
            FileInputStream fis = new FileInputStream(myFile);

            // Finds the workbook instance for XLSX file
            myWorkBook = new XSSFWorkbook (fis);
           
            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
           
            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
           
            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + "\t\t");
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print(cell.getBooleanCellValue() + "\t\t");
                        break;
                    default :
                 
                    }
                }
                System.out.println("");
            }
	    }catch (IOException e) {
	        e.printStackTrace();
	    }
	 	
	}
	public void InitFile(List<String> NomsAlgos){
		
		try {
	        myWorkBook = new XSSFWorkbook ();
	        XSSFSheet mySheet = myWorkBook.createSheet("Sample sheet");
	        // Return first sheet from the XLSX workbook
			 Row row = mySheet.createRow(0);
			//Create a new cell in current row
			
			//Set value to new value
			for(int i=0;i<NomsAlgos.size();i++){
				Cell cell = row.createCell(i);
				cell.setCellValue(NomsAlgos.get(i));
			}
			 FileOutputStream os = new FileOutputStream(new File("C://Users/Zakaria/Documents/Classeur2.xlsx"));
	         myWorkBook.write(os);
	         System.out.println("Writing on XLSX file Finished ...");
		    }catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	public void writeFile(List<String> NomsAlgos,List<List<Double>> Resultats,List<List<Long>> temps){
		try {
        myWorkBook = new XSSFWorkbook();
        XSSFSheet mySheet = myWorkBook.createSheet("Résultats des affectations des puissances");
        XSSFSheet mySheet1 = myWorkBook.createSheet("Temps d'executions des algorithmes");
        // Return first sheet from the XLSX workbook
		 Row row = mySheet.createRow(0);
		 DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		 symbols.setDecimalSeparator('.');
		 DecimalFormat df = new DecimalFormat("#.##",symbols);
		 df.setRoundingMode(RoundingMode.UP);
		//Set value to new value
		for(int i=0;i<NomsAlgos.size();i++){
			//Create a new cell in current row
			Cell cell = row.createCell(i);
			cell.setCellValue(NomsAlgos.get(i));
		}
		for(int j=0;j<Resultats.size();j++){
			List<Double> Resultat=Resultats.get(j);
			Row row1 = mySheet.createRow(j+1);
			for(int i=0;i<Resultat.size();i++){
				Cell cell = row1.createCell(i);
				cell.setCellValue(Double.parseDouble(df.format(Resultat.get(i))));
			}
		}
		//Deuxieme page
		 Row row21 = mySheet1.createRow(0);
		//Set value to new value
				for(int i=0;i<NomsAlgos.size();i++){
					//Create a new cell in current row
					Cell cell = row21.createCell(i);
					cell.setCellValue(NomsAlgos.get(i));
				}
				
		for(int k=0;k<temps.size();k++){
			List<Long> tmp=temps.get(k);
			Row row22 = mySheet1.createRow(k+1);
			for(int i=0;i<tmp.size();i++){
				Cell cell = row22.createCell(i);
				cell.setCellValue(tmp.get(i));
			}
		}
     
		FileOutputStream os = new FileOutputStream(new File(Project.projectPath+"/"+Project.projectName+".xlsx"));
         myWorkBook.write(os);
         System.out.println("Writing on XLSX file Finished ...");
	    }catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Error Excel");
	    }
	}
	public void updateFile(List<List<Integer>> Resultats){
		try {
			 File myFile = new File("C://Users/Zakaria/Documents/Classeur2.xlsx");
	         FileInputStream fis = new FileInputStream(myFile);
        myWorkBook = new XSSFWorkbook (fis);
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
        // Return first sheet from the XLSX workbook
        int rownum = mySheet.getLastRowNum(); 
		//Set value to new value
		for(int j=0;j<Resultats.size();j++){
			List<Integer> Resultat=Resultats.get(j);
			for(int i=0;i<Resultat.size();i++){
				Row row1 = mySheet.createRow(rownum++);
				Cell cell = row1.createCell(j);
				cell.setCellValue(Resultat.get(i));
			}
		}
         FileOutputStream os = new FileOutputStream(myFile);
         myWorkBook.write(os);
         System.out.println("Writing on XLSX file Finished ...");
	    }catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
