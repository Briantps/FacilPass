/**
 * 
 */
package telepeaje;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import ejb.PathFile;
import ejb.Transaction;

import upload.FileUtil;

/**
 * @author tmolina
 *
 */
public class UpTranTelepeajeBean implements Serializable{
	private static final long serialVersionUID = -3273916626394165355L;
	
	@EJB(mappedName="ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName="ejb/PathFile")
	private PathFile pathFile;
		
	List<String> list;
	
	private String path;
    
	/**
	 * Constructor
	 */
	public UpTranTelepeajeBean() {		
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init(){
		path = pathFile.getPath("TelepeajeManual");
		if (path == null) {
			path = "/opt/SDK/upload/telepeaje";
		}	
	}
	 
	// Listener 
	
	/**
	 * Saves the file in the server.
	 */
	public void listener(UploadEvent event){		
		try {
			System.out.println(path);
   			UploadItem item = event.getUploadItem();
            String uploadedFileName = FileUtil.trimFilePath(item.getFileName());
            java.io.File uniqueFile = FileUtil.uniqueFile(new java.io.File(path), uploadedFileName);
            FileUtil.write(uniqueFile, item.getData());  	 
            
            // Read File
            list = new ArrayList<String>();
			Scanner sc = new Scanner(uniqueFile);
			while (sc.hasNext()) {
				list.add(sc.nextLine());
			}
			sc.close();
            
			for (int i = 0; i < list.size(); i++) {
				String []line = list.get(i).split("\\|");
				if (line.length == 8) {
					transaction.upTranTelepeaje(line);
				}
			}
			
        } catch (IOException ex) {
        	System.out.println(" [ Error en FileUploadBean.listener : IOException] ");
            ex.printStackTrace();
        }  catch (Exception e){
        	e.printStackTrace();
        }
	}  	

	
	/**
	 * For code value updates
	 */
	public String reload(){
		FacesContext.getCurrentInstance().renderResponse();
		return null;
	}
	
	/**
	 * For refresh the page
	 */
	public void refresh() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    Application application = context.getApplication();
	    UIViewRoot viewRoot = application.getViewHandler().createView(context, context.getViewRoot().getViewId());
	    context.setViewRoot(viewRoot);
	    context.renderResponse(); 
	  }

	// Getters and Setters --------
	
	/**
	 * 
	 * @return currentTimeMillis
	 */
	public long getTimeStamp(){
        return System.currentTimeMillis();
    }
}