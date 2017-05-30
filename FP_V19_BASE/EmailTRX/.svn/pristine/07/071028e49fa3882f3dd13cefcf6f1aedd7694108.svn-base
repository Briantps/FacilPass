package logs;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {

	public static Logger notificationLog;
	public static Logger errorLog;
	
	public Log(){
		
		PropertyConfigurator.configure("log4j.properties");
		
		notificationLog= Logger.getLogger("Notifications");
		errorLog= Logger.getLogger("Errors");
		
		notificationLog.info("Se inicia controlador de logs informativos");
		errorLog.info("Se inicia controlador de logs de errores");
		
	}
	
	public Logger getNotificationLog(){
		return notificationLog;
	}
		
	public Logger getErrorLog(){
		return errorLog;
	}
}
