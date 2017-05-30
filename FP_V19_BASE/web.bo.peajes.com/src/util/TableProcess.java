package util;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Objeto que manejara lo que se visualiza por ver procesos
 * @author ablasquez
 *
 */
public class TableProcess implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String detailProcess;
	private Timestamp dateProcess;
	private String stateProcess;
	private String userCreateProcess;
	private boolean processError;
	private String fileNameXMLResponce;
	private String fileNameXMLRequest;
	private boolean processPSE;
	private Long pseId;
	private String filePse;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDetailProcess() {
		return detailProcess;
	}
	public void setDetailProcess(String detailProcess) {
		this.detailProcess = detailProcess;
	}
	public Timestamp getDateProcess() {
		return dateProcess;
	}
	public boolean getProcessError(){
		return processError;
	}
	
	public void setProcessError(boolean procesError){
		this.processError=procesError;
	}
	public void setDateProcess(Timestamp dateProcess) {
		this.dateProcess = dateProcess;
	}
	public String getStateProcess() {
		return stateProcess;
	}
	public void setStateProcess(String stateProcess) {
		this.stateProcess = stateProcess;
	}
	public String getUserCreateProcess() {
		return userCreateProcess;
	}
	public void setUserCreateProcess(String userCreateProcess) {
		this.userCreateProcess = userCreateProcess;
	}
	
	public String getFileNameXMLResponce() {
		return fileNameXMLResponce;
	}
	public void setFileNameXMLResponce(String fileNameXMLResponce) {
		this.fileNameXMLResponce = fileNameXMLResponce;
	}
	public String getFileNameXMLRequest() {
		return fileNameXMLRequest;
	}
	public void setFileNameXMLRequest(String fileNameXMLRequest) {
		this.fileNameXMLRequest = fileNameXMLRequest;
	}
	public boolean isProcessPSE() {
		return processPSE;
	}
	public void setProcessPSE(boolean processPSE) {
		this.processPSE = processPSE;
	}
	public Long getPseId() {
		return pseId;
	}
	public void setPseId(Long pseId) {
		this.pseId = pseId;
	}
	public String getFilePse() {
		return filePse;
	}
	public void setFilePse(String filePse) {
		this.filePse = filePse;
	}
}