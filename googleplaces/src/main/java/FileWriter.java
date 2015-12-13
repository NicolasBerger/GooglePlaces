

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import se.walkercrou.places.Hours.Period;
import se.walkercrou.places.Place;

public class FileWriter {
	
	private boolean WRITE;
	
	private HSSFWorkbook workbook = new HSSFWorkbook();
	private HSSFSheet sheet = workbook.createSheet("Feuille 1");
	private int rownum = 0;
	private String[] titres = new String[]{"ID","Name","Address","ZipCode","Phone","Intern. Phone","Website","Always Opened","Status","URL","Price","Vicinity","Nb Avis","Horaires1","Horaires2","Horaires3","Horaires4","Horaires5","Horaires6","Horaires7"};

	public FileWriter(boolean writeExcel) {
		this.WRITE = writeExcel;
		if(this.WRITE){
			init();
		}
	}

	private void init(){
		//Mise en place des titres
		Row row = sheet.createRow(rownum++);
		for (int i=0;i<titres.length;i++) {
			row.createCell(i).setCellValue(titres[i]);
		}
	}
	
	public void writePlace(String placeID, Place detail) {
		if(this.WRITE){
			//Crée chaque ligne
		    HSSFRow row = sheet.createRow(rownum++);
		    int cellnum = 0;
		    row.createCell(cellnum++).setCellValue(placeID);
		    
			if(null!=detail){
				System.out.println(detail.getPlaceId() + " | " + detail.getName() + " | " + detail.getTypes().stream().reduce((a,b)->a+" "+b).get());
				row.createCell(cellnum++).setCellValue(detail.getName());
				row.createCell(cellnum++).setCellValue(detail.getAddress());
				row.createCell(cellnum++).setCellValue(extractParisZpiCode(detail.getAddress()));
				row.createCell(cellnum++).setCellValue(detail.getPhoneNumber());
				row.createCell(cellnum++).setCellValue(detail.getInternationalPhoneNumber());
				row.createCell(cellnum++).setCellValue(detail.getWebsite());
				row.createCell(cellnum++).setCellValue(detail.isAlwaysOpened());
				row.createCell(cellnum++).setCellValue(detail.getStatus().toString());
				row.createCell(cellnum++).setCellValue(detail.getGoogleUrl());
				row.createCell(cellnum++).setCellValue(detail.getPrice().toString());
				row.createCell(cellnum++).setCellValue(detail.getVicinity());
				row.createCell(cellnum++).setCellValue(detail.getReviews().size());
				if(null!=detail.getHours() && null!=detail.getHours().getPeriods()){
					List<Period> periods = detail.getHours().getPeriods();
					for (Period period : periods) {
						row.createCell(cellnum++).setCellValue(period.toString());
					}
				}
	//			row.createCell(cellnum++).setCellValue(detail.getHours());
			}	 
		}
	}
	
	public File write(){
		if(this.WRITE){
			try {
				File file = new File("C:\\Users\\Nouvel Utilisateur\\Desktop\\Google Places.xls");
			    FileOutputStream out = new FileOutputStream(file);
			    workbook.write(out);
			    out.close();
			    System.out.println("Excel written successfully..");
			    return file;
			} catch (FileNotFoundException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			return null;
		}
			return null;
	}
	
	private static String extractParisZpiCode(String address){
		String res = "NONE";
		if(null != address){
			int index = address.lastIndexOf("750");
			if(index != -1){
				res = address.substring(index,index+5);
			}
		}
		return res;
	}
}
