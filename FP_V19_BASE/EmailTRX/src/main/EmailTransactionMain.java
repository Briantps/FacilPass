package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import entity.EntityDaoEmailTransaction;

/**
 * @author NRamírez
 * Todo parametro que se adhesion aqui debe ser insertado en la tabla TB_EMAIL_PROCESS en la Base de Datos
 */


public class EmailTransactionMain {

	//public static String version="4.0.9.19.190215"; 
	//public static String version="5.0.1.20.290515";
	//public static String version="4.0.10.20.230615"; //versión 15 por si lo piden
	//public static String version="4.0.11.21.010715"; //versión 15 por si lo piden
	//public static String version="4.0.13.23.031115";
	//public static String version="5.0.3.22.230615";
	//public static String version="5.0.4.23.010715";//versión 16
	//public static String version="5.0.5.24.011015";//versión 16
	//public static String version="5.0.2.26.031115";//versión 16
	//public static String version="5.0.3.27.111115";//versión 16
	//public static String version="5.0.6.30.101215";//versión 16
	//public static String version="5.0.9.33.050716";
	//public static String version="6.0.4.34.060716";
	//public static String version="6.0.5.35.120716";
	//public static String version="6.0.6.36.250716";
	//public static String version="6.0.7.37.090816";
	public static String version="7.0.1.38.260417";
	
	private static EntityDaoEmailTransaction entityDao;
	
	public static Properties properties;
	
	public static int frequency;
	
	public static logs.Log logs;
	
	public static int cantMessages;
	private static ThreadEmaiTransaction tet = null;
	private static Thread thread = null;
	
	public static void main(String args[]){
				
		System.setErr(System.out);
		
		properties= new Properties();	
		try {
		
			properties.load(new FileInputStream(new File("comm.properties")));
			
			try{
				frequency  = Integer.parseInt(properties.getProperty("frequency"));
				cantMessages  = Integer.parseInt(properties.getProperty("cantMessages"));
			}
			catch(Exception ex){
				frequency=60000;
				cantMessages=50;
				EmailTransactionMain.logs.getErrorLog().error("Error al cargar parámetros del archivo comm.properties");
				EmailTransactionMain.logs.getErrorLog().error("main.Exception: ",ex);
				ex.printStackTrace();
			}
			
			} catch (FileNotFoundException e) {
			EmailTransactionMain.logs.getErrorLog().error("main.FileNotFoundException: ",e);
			} catch (IOException e) {
				EmailTransactionMain.logs.getErrorLog().error("main.IOException: ",e);
				e.printStackTrace();
			}
		try{
			System.out.println("version No: " + version);
			System.out.println("frequency: " + frequency);
			System.out.println("cantMessages: " + cantMessages);
			
			logs= new logs.Log();
			
			EmailTransactionMain.logs.getNotificationLog().info("version No: " + version);
			EmailTransactionMain.logs.getNotificationLog().info("frequency: " + frequency);
			EmailTransactionMain.logs.getNotificationLog().info("cantMessages: " + cantMessages);
			
			tet= new ThreadEmaiTransaction();
			thread = new Thread(tet);
			thread.start();
			EmailTransactionMain.logs.getNotificationLog().info("Hilo inicializado...");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static EntityDaoEmailTransaction getEntityDAO() {
		try{
			if (entityDao == null) {
				entityDao = new EntityDaoEmailTransaction();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return entityDao;
	}
	
	
	
}
