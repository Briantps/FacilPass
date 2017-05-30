package configSystem;

import java.io.Serializable;
import java.util.ArrayList;



import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import ejb.TextEditorManager;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.ReToolbarOptionActionList;

public class TextEditorManagerBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/TextEditorManager")
	private TextEditorManager TextEditorManagerEJB;
	
	private UserLogged usrs;
	private String ip;
	private ArrayList<ReToolbarOptionActionList> listEditor;
	private ArrayList<ReToolbarOptionActionList> tempListEditor;
	private boolean modal;
	private boolean modaError;
	private String msn;
	private String msnError;
	private boolean modalOption;
	
	
	/** Constructor **/
	public TextEditorManagerBean (){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");	
		ip=SessionUtil.ip();
	}
	@PostConstruct
	public void init (){
		
		if (usrs.getUserId()!=null){
			System.out.println("sesion de Usuario : " + usrs.getUserId());	
			
			Long cont = TextEditorManagerEJB.getEditorListCount();
			
			if (cont == 0){
				this.setMsnError("No se Encontraron resultados, verifique las opciones");
				System.out.println("No se Encontraron resultados, verifique las opciones");
				
			}else if (cont == -1L){
				this.setMsnError("Error en la Busqueda");
				System.out.println("Error en la Busqueda");
				this.setModaError(true);
			}else{
				System.out.println("Cont: " + cont);
				listEditor = new ArrayList<ReToolbarOptionActionList>();
				tempListEditor = new ArrayList<ReToolbarOptionActionList>();
				
				listEditor = TextEditorManagerEJB.getEditorList();
				tempListEditor = TextEditorManagerEJB.getEditorList();
			}
		}			
		else{
			System.out.println("sesion de Usuario Invalida :" + usrs.getUserId());			
		}
	}
	public void confirm (){
		this.setMsn("¿Está seguro de guardar los cambios?");
		this.setModalOption(true);
	}
	public void saveSettings (){
		hideModal();
		this.setMsn(TextEditorManagerEJB.setUpdateBarEditor(listEditor));
		this.setModal(true);
	}

	public void cancelSettings (){
		this.setMsn("");
		this.setMsnError("");
		this.setModal(false);
		this.setModaError(false);
		this.setModalOption(false);
		init();
	}

	public void hideModal (){
		this.setMsn("");
		this.setMsnError("");
		this.setModal(false);
		this.setModaError(false);
		this.setModalOption(false);
	}
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public ArrayList<ReToolbarOptionActionList> getListEditor() {
		return listEditor;
	}

	public void setListEditor(ArrayList<ReToolbarOptionActionList> listEditor) {
		this.listEditor = listEditor;
	}
	public boolean isModal() {
		return modal;
	}
	public void setModal(boolean modal) {
		this.modal = modal;
	}
	public boolean isModaError() {
		return modaError;
	}
	public void setModaError(boolean modaError) {
		this.modaError = modaError;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getMsnError() {
		return msnError;
	}
	public void setMsnError(String msnError) {
		this.msnError = msnError;
	}
	public boolean isModalOption() {
		return modalOption;
	}
	public void setModalOption(boolean modalOption) {
		this.modalOption = modalOption;
	}
	public ArrayList<ReToolbarOptionActionList> getTempListEditor() {
		return tempListEditor;
	}
	public void setTempListEditor(
			ArrayList<ReToolbarOptionActionList> tempListEditor) {
		this.tempListEditor = tempListEditor;
	}
	
}
