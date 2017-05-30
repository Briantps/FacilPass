package util;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParserReadFiles {
	
	private static int colsDef=8;
	
	public static TableModel parseCSV(File f,String delimiter) throws FileNotFoundException { 
		try{
			ArrayList<String> headers = new ArrayList<String>(); 
			ArrayList<String> oneDdata = new ArrayList<String>(); 
			// Get the headers of the table. 
			Scanner lineScan = new Scanner(f); 
			Scanner s = new Scanner(lineScan.nextLine());
			System.out.println("delimiter:"+delimiter);
			s.useDelimiter(delimiter); 
			while (s.hasNext()) {
				if(headers.size()<colsDef){
					headers.add(s.next());
				}else{
					System.out.println("break");
					break;
				}
			} // Go through each line of the table and add each cell to the ArrayList
			while (lineScan.hasNextLine()) {
				String lin=lineScan.nextLine();
				String[] spl=lin.split(delimiter,-1);
				int colData=(spl.length>colsDef?colsDef:spl.length);
				for(int i=0;i<colData;i++){
					oneDdata.add(spl[i]);
				}
			} 
			String[][] data = 
				new String[oneDdata.size()/headers.size()][headers.size()];
			// Move the data into a vanilla array so it can be put in a table.
			int col=oneDdata.size()/headers.size();
			for (int x = 0; x < col; x++) {
				for (int y = 0; y < headers.size(); y++) {
					data[x][y] = oneDdata.remove(0);
				} 
			} // Create a table and return it.
			return new DefaultTableModel(data, headers.toArray());
		}catch(Exception e){
			System.out.println(" [ ParserReadFiles.parseCSV ] ");
			e.printStackTrace();
			return null;
		}
	}
	public static TableModel parseXLS(File f) throws FileNotFoundException { 
		try {
			ArrayList<String> headers = new ArrayList<String>();
			Workbook fileExcel = Workbook.getWorkbook(f); 
			System.out.println("Número de Hojas\t" 
			+ fileExcel.getNumberOfSheets());
			String[][] data = null;
			for (int sheetNo = 0; sheetNo < fileExcel.getNumberOfSheets(); sheetNo++){ 
				// Recorre 
				// cada    
				// hoja                                                                                                                                                       
				Sheet hoja = fileExcel.getSheet(sheetNo); 
				int numColumnas = (hoja.getColumns()>colsDef?colsDef:hoja.getColumns()); 
				int numFilas = hoja.getRows(); 
				data = 
					new String[numFilas-1][numColumnas];
				System.out.println("Nombre de la Hoja\t" 
				+ fileExcel.getSheet(sheetNo).getName());
				for (int columna = 0; columna < numColumnas; columna++) { // Recorre                                                                                
					// cada                                                                                
					// columna                                                                            
					// de                                                                                
					// la                                                                                
					// fila
					headers.add(hoja.getCell(columna, 0).getContents());
				} 
				for (int fila = 1; fila < numFilas; fila++) { // Recorre cada 
					// fila de la 
					// hoja 
					for (int columna = 0; columna < numColumnas; columna++) { // Recorre                                                                                
						// cada                                                                                
						// columna                                                                            
						// de                                                                                
						// la                                                                                
						// fila 
						data[fila-1][columna] = hoja.getCell(columna, fila).getContents(); 
					} 
				} 
			} 
			return new DefaultTableModel(data, headers.toArray());
		} catch (Exception ioe) { 
			System.out.println(" [ ParserReadFiles.parseXLS ] ");
			ioe.printStackTrace(); 
			return null;
		}  
	}
	public static TableModel parseTXT(File f) throws FileNotFoundException {
		try{
			ArrayList<String> lineas = new ArrayList<String>();
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream(f);
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fstream,"UTF8"));
            String strLinea;
            // Leer el archivo linea por linea
            while ((strLinea = buffer.readLine()) != null)   {
                // Imprimimos la línea por pantalla
            	lineas.add(strLinea);
            }
            // Cerramos el archivo
            entrada.close();
            buffer.close();
            String[][] data = 
    			new String[lineas.size()][colsDef];
            for(int i=0;i<lineas.size();i++){
            	String[] linea=lineas.get(i).split(Pattern.quote("|"),-1);
            	int sizeLine=(linea.length>colsDef?colsDef:linea.length);
            	for(int x=0;x<sizeLine;x++){
            		data[i][x]=linea[x];
            	}
            }
            return new DefaultTableModel(data,new String[]{"PLACA",
            		"ADICIONAL1","ADICIONAL2","ADICIONAL3","CATEGORÍA","LÍNEA",
            		"ESPECIAL","OBSERVACIÓN"});
        }catch (Exception e){ //Catch de excepciones
        	System.out.println(" [ ParserReadFiles.parseTXT ] ");
        	e.printStackTrace();
            return null;
        }
	}
	public static TableModel parseXLSX(File f) throws FileNotFoundException { 
		try{
			FileInputStream fileInputStream = new FileInputStream(f);
			XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
			XSSFSheet hssfSheet = workBook.getSheetAt(0);
			ArrayList<String> headers = new ArrayList<String>();
			String[][] data = new String[hssfSheet.getLastRowNum()][colsDef];
			for(Row row : hssfSheet) { 
				int lastCelNum=(row.getLastCellNum()>colsDef?colsDef:row.getLastCellNum());
				if(row.getRowNum()==0){
					for(int cn=0; cn<lastCelNum; cn++) {  
						// If the cell is missing from the file, generate a blank one  
						Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);  
						// Print the cell for debugging  
						headers.add(cell.toString());
					}
				}else{
					for(int cn=0; cn<lastCelNum; cn++) {  
						// If the cell is missing from the file, generate a blank one  
						Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);  
						// Print the cell for debugging
						if(cn==4){
							if((cell.toString())==null||(cell.toString()).equals("")){
								data[row.getRowNum()-1][cn] = cell.toString();
							}else{
								try{
									data[row.getRowNum()-1][cn] = String.valueOf((long)cell.getNumericCellValue());
								}catch (Exception e) {
									System.out.println("Categoria no numero.");
									data[row.getRowNum()-1][cn] = cell.toString();
								}
							}
						}else{
							data[row.getRowNum()-1][cn] = cell.toString();
						}
					}
				}
			}
			return new DefaultTableModel(data, headers.toArray());
		}catch (Exception e){
			System.out.println(" [ ParserReadFiles.parseXLSX ] ");
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static TableModel readXLS(File f) throws FileNotFoundException { 
		System.out.println(" [ readXLS ] ");
		try {
			ArrayList<String> headers = new ArrayList<String>();
			Workbook fileExcel = Workbook.getWorkbook(f); 
			System.out.println("Número de Hojas\t" 
			+ fileExcel.getNumberOfSheets());
			String[][] data = null;
			//for (int sheetNo = 0; sheetNo < fileExcel.getNumberOfSheets(); sheetNo++){ 
			for (int sheetNo = 0; sheetNo < 1; sheetNo++){ 
				// Recorre 
				// cada    
				// hoja                                                                                                                                                       
				Sheet hoja = fileExcel.getSheet(sheetNo); 
				int numColumnas = hoja.getColumns(); 
				int numFilas = hoja.getRows(); 
				
				System.out.println(" [numColumnas] "+numColumnas);
				System.out.println(" [numFilas] "+numFilas);
				
				data = new String[numFilas][numColumnas];

				System.out.println("Nombre de la Hoja\t" 
				+ fileExcel.getSheet(sheetNo).getName());
				for (int columna = 0; columna < numColumnas; columna++) { // Recorre                                                                                
					// cada                                                                                
					// columna                                                                            
					// de                                                                                
					// la                                                                                
					// fila
					headers.add(hoja.getCell(columna, 0).getContents());
				} 
				for (int fila = 0; fila < numFilas; fila++) { // Recorre cada 
					// fila de la 
					// hoja 
					for (int columna = 0; columna < numColumnas; columna++) { // Recorre                                                                                
						// cada                                                                                
						// columna                                                                            
						// de                                                                                
						// la                                                                                
						// fila 
						data[fila][columna] = hoja.getCell(columna, fila).getContents(); 
					} 
				} 
			} 
			/*return new DefaultTableModel(data,new String[]{"ID_INTERNO",
	        		"ID_FACIAL","ID_COURIER","ID_ROLLO"});*/
			return new DefaultTableModel(data, headers.toArray());
		} catch (Exception ioe) { 
			System.out.println(" [ ParserReadFiles.parseXLS ] ");
			ioe.printStackTrace(); 
			return null;
		}  
	}
	
	public static TableModel readXLSX(File f) throws FileNotFoundException { 
		System.out.println(" [ readXLSX ] ");
		try{
			FileInputStream fileInputStream = new FileInputStream(f);
			XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
			XSSFSheet hssfSheet = workBook.getSheetAt(0);
			String[][] data = new String[hssfSheet.getLastRowNum()+1][4];
			
			if (hssfSheet.getPhysicalNumberOfRows()==0){
				System.out.println("El archivo no contiene datos.");
				return new DefaultTableModel(0,0);
			}
			else if (hssfSheet.getPhysicalNumberOfRows()>0){
				for(Row row : hssfSheet) { 
					for(int cn=0; cn<row.getLastCellNum(); cn++) {  
						// If the cell is missing from the file, generate a blank one  
						Cell cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);  
						// Print the cell for debugging
						if(cn==2 || cn==3){
							if((cell.toString())==null||(cell.toString()).equals("")){
								data[row.getRowNum()][cn] = cell.toString();
							}else{
								try{
									data[row.getRowNum()][cn] = String.valueOf((long)cell.getNumericCellValue());	
								}catch (Exception e) {
									System.out.println("Id interno no númerico------>.");
									data[row.getRowNum()][cn] = cell.toString();
								}
							}
						}else{
							data[row.getRowNum()][cn] = cell.toString();
						}
					}
				}
			  }
			return new DefaultTableModel(data, new String[]{"ID_INTERNO",
	        		"ID_FACIAL","ID_COURIER","ID_ROLLO"});
			}catch (ArrayIndexOutOfBoundsException e) {
			         System.out.println("El archivo contiene mas columnas.");
			    return new DefaultTableModel(100,100);
			}
			catch (Exception e){
				System.out.println(" [ ParserReadFiles.parseXLSX ] ");
				e.printStackTrace();
				return null;
			}
	}
	
}