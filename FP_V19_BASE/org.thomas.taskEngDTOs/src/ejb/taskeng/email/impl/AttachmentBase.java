package ejb.taskeng.email.impl;

import java.io.File;

import ejb.taskeng.email.Attachment;


/**
 * Implementacion de Attachment. La ruta de archivo adjunto
 * se almacena como un String en memoria.
 * @author Mauricio Gil
 */
public class AttachmentBase implements Attachment {

	private static final long serialVersionUID = -6219962309482410323L;
	
	private String filePath;

	public AttachmentBase(){
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public File getFile(){
		return new File(filePath);
	}
}
