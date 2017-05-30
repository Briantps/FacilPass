package process.device.blackList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.DeviceBlackList;
import ejb.report.Report;

public class EnterDeviceBlackListBean implements Serializable {

	private static final long serialVersionUID = 8162813547847156020L;
	
	private String tagId = "";
	
	List<String> list;
	
	private boolean showConfirmationModal;
	
	private String modalConfirmationMessage;
	
	private boolean showMessage;
	
	private String message;
	
	@EJB(mappedName = "ejb/Report")
	private Report report;
	
	@EJB(mappedName = "ejb/DeviceBlackList")
	private DeviceBlackList devicebl;
	
	public EnterDeviceBlackListBean(){
		clear();
	}

	
	public List<String> autocomplete(Object suggest) {
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();
		if (list == null) {
			list = report.getDeviceCodes();
		}
		else{
			result.clear();
			list = report.getDeviceCodes();
		}
		Iterator<String> iterator = list.iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setShowConfirmationModal(boolean showConfirmationModal) {
		this.showConfirmationModal = showConfirmationModal;
	}

	public boolean isShowConfirmationModal() {
		return showConfirmationModal;
	}

	public void setModalConfirmationMessage(String modalConfirmationMessage) {
		this.modalConfirmationMessage = modalConfirmationMessage;
	}

	public String getModalConfirmationMessage() {
		return modalConfirmationMessage;
	}
	
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public boolean isShowMessage() {
		return showMessage;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setDeviceBlackList(DeviceBlackList deviceBlackList) {
		this.devicebl = deviceBlackList;
	}

	public DeviceBlackList getDeviceBlackList() {
		return devicebl;
	}

	public String validate(){	 	
	 if(!tagId.equals("")){
		 if(validateHexa(tagId.toUpperCase())==false) {
			 message = "El Id del Dispositivo no es Válido. Solo se acepta Valores Hexadecimales";
			 showMessage = true;
		} else if(tagId.length()%2!=0){
			message = "El Id del Dispositivo no es Válido. La Longitud debe ser par";
			showMessage =true;	
		} else{		 
			modalConfirmationMessage="¿Está Seguro de Ingresar el Dispositivo "+tagId+" a Lista Negra?";
			showConfirmationModal = true;
		}
	 } else {
		 message = "Falta ingresar Id de Dispositivo";
		 showMessage =  true;
	 }
	 
		return null;
	}
	
	public void hideModal(){
		modalConfirmationMessage="";
		showConfirmationModal = false;		
	}
	
	public void hide(){
		message="";
		showMessage = false;		
	}
	
	public void clear(){
		tagId = "";
		modalConfirmationMessage="";
		showConfirmationModal = false;	
		showMessage = false;
		message ="";

	}
	
	public String aplicar(){
		System.out.println("---"+tagId);
		if(devicebl.enterDeviceBL(tagId,SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			
			message = "Cambio realizado correctamente";
			showMessage =  true;
			modalConfirmationMessage="";
			showConfirmationModal = false;
			tagId = "";
		} else{
			modalConfirmationMessage="";
			showConfirmationModal = false;	
			message = "Se ha presentado un Error. Verifique que el dispositivo no se encuentre ya en lista negra.";
			showMessage =  true;			
		}
		return null;
	}
	
	public boolean validateHexa(String device){
		boolean result = false;
		int tamanio = device.length();
		char dato;
		for (int i=0; i<tamanio; i++){
			dato = device.charAt(i);
			if ((((int)dato) >=48) && (((int)dato) <=57)){
				result =  true;
			} else if ((((int)dato) >=65) && (((int)dato) <=70)){
				result =  true;
			} else {
				result =  false;
				break;
			}
		}
			
		return result;
	}
}
